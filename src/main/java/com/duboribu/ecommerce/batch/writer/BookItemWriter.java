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
                Optional<Book> byTitle = bookJpaRepository.findByTitle(book.getTitle());

                // 기존 책이 아닐경우만 s3 업로드
                // 기존 책인 경우 일단 업데이트 처리하지않는 배치
                if (!byTitle.isPresent()) {
                    // src s3 저장
                    String imageS3Url = s3Uploader.uploadToS3(s3Uploader.downloadImage(book.getFilePath()), "image");
                    book.updatePath(imageS3Url);
                }
                
                // 저장
                Book savedBook = bookJpaRepository.save(book);
                
                // Stock이 없으면 기본 재고 생성
                if (savedBook.getStock() == null) {
                    Stock stock = new Stock(savedBook, 100);
                    stockJpaRepository.save(stock);
                    log.info("재고 생성: {} - 100개", savedBook.getTitle());
                }
                
                log.info("책 저장 완료: {} (ID: {})", savedBook.getTitle(), savedBook.getId());
                
            } catch (Exception e) {
                log.error("책 저장 실패: {} - {}", book.getTitle(), e.getMessage());
            }
        }
    }
}

