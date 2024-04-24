package com.duboribu.ecommerce.admin.item.dto;

import com.duboribu.ecommerce.entity.Book;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class ResponseBook {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String filePath;
    private int price;
    private Long category;
    public ResponseBook(Book savedBook) {
        this.id = savedBook.getId();
        this.title = savedBook.getTitle();
        this.author = savedBook.getAuthor();
        this.publisher = savedBook.getAuthor();
        BigDecimal value = savedBook.getPrices().get(0).getValue();
        price = value.intValue();
        this.filePath = savedBook.getFilePath();
    }
    @QueryProjection
    public ResponseBook(Long id, String title, String author, String publisher, String filePath, int price, Long category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.filePath = filePath;
        this.price = price;
        this.category =category;
    }

}
