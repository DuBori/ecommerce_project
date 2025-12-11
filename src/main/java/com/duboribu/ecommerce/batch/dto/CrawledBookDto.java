package com.duboribu.ecommerce.batch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CrawledBookDto {
    private String title;
    private String author;
    private String publisher;
    private int price;
    private String comment;
    private String information;
    private String filePath;
    private String categoryCode;
    private String categoryName;
}

