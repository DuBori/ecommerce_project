package com.duboribu.ecommerce.admin.item.dto;

import com.duboribu.ecommerce.entity.Book;
import com.duboribu.ecommerce.front.enums.State;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseBook {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private int price;
    private String comment;
    private String information;
    private int weight;
    private Long category;
    private String filePath;
    private String state;
    public ResponseBook(Book savedBook) {
        this.id = savedBook.getId();
        this.title = savedBook.getTitle();
        this.author = savedBook.getAuthor();
        this.publisher = savedBook.getAuthor();
        price = savedBook.getPrice();
        this.filePath = savedBook.getFilePath();
    }
    @QueryProjection
    public ResponseBook(Long id, String title, String author, String publisher, int price, String comment, String information, int weight, Long category, String filePath, State state) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.comment = comment;
        this.information = information;
        this.weight = weight;
        this.category = category;
        this.filePath = filePath;
        this.state = state.name();
    }
}
