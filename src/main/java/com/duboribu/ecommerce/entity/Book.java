package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.item.dto.CreateBookRequest;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Book 엔티티
@Entity
@DiscriminatorValue("B")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {
    private String title;
    private String author;
    private String publisher;

    public Book(String title, String author, String publisher, String productCode, Stock stock) {
        super(productCode, stock);
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }

    public Book(String title, String author, String publisher) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }

    public Book(CreateBookRequest request) {
        this.title = request.getTitle();
        this.author = request.getAuthor();
        this.publisher = request.getPublisher();
    }
}