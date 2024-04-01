package com.duboribu.ecommerce.admin.item.dto;

import com.duboribu.ecommerce.entity.Book;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ResponseItem {
    private String title;
    private String author;
    private String publisher;
    private int price;
    public ResponseItem(Book savedBook) {
        this.title = savedBook.getTitle();
        this.author = savedBook.getAuthor();
        this.publisher = savedBook.getAuthor();
        BigDecimal value = savedBook.getPrices().get(0).getValue();
        price = value.intValue();
    }
}
