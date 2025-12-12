package com.duboribu.ecommerce.batch.processor;

import com.duboribu.ecommerce.admin.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.batch.dto.CrawledBookDto;
import com.duboribu.ecommerce.entity.Book;
import com.duboribu.ecommerce.entity.Category;
import com.duboribu.ecommerce.repository.BookJpaRepository;
import com.duboribu.ecommerce.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("bookItemProcessor")
@RequiredArgsConstructor
public class BookItemProcessor implements ItemProcessor<CrawledBookDto, Book> {

    private final BookJpaRepository bookJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Book process(CrawledBookDto dto) {
        log.info("Processing: {}", dto.getTitle());
        
        // 1. 제목으로 기존 데이터 확인
        Optional<Book> existingBook = bookJpaRepository.findByTitle(dto.getTitle());
        
        // 2. 카테고리 조회 (code로 조회, 없으면 name으로 조회)
        Category category = findCategory(dto.getCategoryCode(), dto.getCategoryName());
        
        if (existingBook.isPresent()) {
            // 기존 데이터가 있으면 업데이트 스킵 (또는 업데이트 로직 추가 가능)
            log.info("기존 책 존재 - 스킵: {}", dto.getTitle());
            return null; // null 반환 시 Writer로 전달되지 않음
        }

        // 3. 신규 데이터 생성
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(dto.getTitle());
        request.setAuthor(dto.getAuthor());
        request.setPublisher(dto.getPublisher());
        request.setPrice(dto.getPrice() > 0 ? dto.getPrice() : 99999); // 기본 가격
        request.setComment(dto.getComment());
        request.setInformation(dto.getInformation());
        request.setFilePath(dto.getFilePath());
        request.setCategory(category != null ? category.getId() : null);
        request.setState("Y");
        request.setWeight(500);

        log.info("신규 책 생성 준비: {}", dto.getTitle());
        return new Book(request, category);
    }

    private Category findCategory(String categoryCode, String categoryName) {
        // code로 먼저 조회
        Optional<Category> byCode = categoryJpaRepository.findByCode(categoryCode);
        if (byCode.isPresent()) {
            return byCode.get();
        }

        // 책 판매 고정
        Category category = new Category(categoryName, "book", "Y", null);
        Category parentNewCate = categoryJpaRepository.save(category);
        return categoryJpaRepository.save(new Category(categoryName, categoryCode, "Y", parentNewCate));
    }
}

