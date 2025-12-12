package com.duboribu.ecommerce.batch.writer;

import com.duboribu.ecommerce.entity.Book;
import com.duboribu.ecommerce.entity.Stock;
import com.duboribu.ecommerce.repository.BookJpaRepository;
import com.duboribu.ecommerce.repository.StockJpaRepository;
import com.duboribu.ecommerce.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component("bookItemWriter")
@RequiredArgsConstructor
public class BookItemWriter implements ItemWriter<Book> {

    private final BookJpaRepository bookJpaRepository;
    private final StockJpaRepository stockJpaRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public void write(Chunk<? extends Book> books) {
        log.info("Writing {} books", books.size());
        
        for (Book book : books) {
            try {
                // 기존 책 확인
                Optional<Book> existingBook = bookJpaRepository.findByTitle(book.getTitle());

                // 기존 책이면 스킵 (저장 안 함)
                if (existingBook.isPresent()) {
                    log.info("기존 책 존재 - 스킵: {}", book.getTitle());
                    continue;
                }
                
                // 신규 책만 S3 업로드
                try {
                    String imageS3Url = s3Uploader.uploadToS3(
                            s3Uploader.downloadImage(book.getFilePath()), 
                            "image"
                    );
                    book.updatePath(imageS3Url);
                    log.info("S3 업로드 완료: {}", book.getTitle());
                } catch (Exception e) {
                    log.warn("S3 업로드 실패 - 원본 URL 유지: {} - {}", book.getTitle(), e.getMessage());
                }

                // 신규 책 저장
                Book savedBook = bookJpaRepository.save(book);
                
                // Stock 생성
                if (savedBook.getStock() == null) {
                    Stock stock = new Stock(savedBook, 100);
                    stockJpaRepository.save(stock);
                    log.info("재고 생성: {} - 100개", savedBook.getTitle());
                }
                
                log.info("신규 책 저장 완료: {} (ID: {})", savedBook.getTitle(), savedBook.getId());
                
            } catch (Exception e) {
                log.error("책 저장 실패: {} - {}", book.getTitle(), e.getMessage());
            }
        }
    }
}
