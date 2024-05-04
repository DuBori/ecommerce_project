package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.admin.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.admin.item.dto.UpdateBookRequest;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// Book 엔티티
@Entity
@DiscriminatorValue("B")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item  implements Serializable {
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

    public Book(CreateBookRequest request, Category category) {
        super(request, category);
        this.title = request.getTitle();
        this.author = request.getAuthor();
        this.publisher = request.getPublisher();

    }

    public void updateItem(UpdateBookRequest request) {
        if (request.getId() <= 0) {
            throw new IllegalArgumentException("상품 수정 에러");
        }
        super.updateItemInfo(request.getFilePath(), request.getPrice(), request.getComment(), request.getInformation(), request.getWeight());
        this.title = request.getTitle();
        this.author = request.getAuthor();
        this.publisher = request.getPublisher();
    }

}