package com.duboribu.ecommerce.admin.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateBookRequest {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String filePath;
    private int price;
    private String comment;
    private String information;
    private int weight;
    private int count;
}
