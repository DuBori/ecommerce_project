package com.duboribu.ecommerce.admin.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CreateBookRequest {
    private String title;
    private String author;
    private String publisher;
    private int price;
    private String filePath;

    public CreateBookRequest(String title, String author, String publisher, int price) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
    }
}
