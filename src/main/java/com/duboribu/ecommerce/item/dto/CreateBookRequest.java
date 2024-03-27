package com.duboribu.ecommerce.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateBookRequest {
    private String title;
    private String author;
    private String publisher;
    private int price;
}
