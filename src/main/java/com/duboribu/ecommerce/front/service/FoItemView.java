package com.duboribu.ecommerce.front.service;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class FoItemView {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String filePath;
    private int price;
    private int stockCount;
    @QueryProjection
    public FoItemView(Long id, String title, String author, String publisher, String filePath, BigDecimal price, int stockCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.filePath = filePath;
        this.price = price.intValue();
        this.stockCount = stockCount;
    }
}
