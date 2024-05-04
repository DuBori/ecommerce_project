package com.duboribu.ecommerce.front.order.dto;

import com.duboribu.ecommerce.front.enums.State;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;
@Getter
@ToString
public class FoOrderItemView {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String filePath;
    private int unitPrice;
    private int quantity;
    private int price;
    private int stockCount;
    private String state;
    private String comment;
    private String information;
    private int weight;
    private Long cartItemId;
    @QueryProjection
    public FoOrderItemView(Long id, String title, String author, String publisher, String filePath,
                      int price, int stockCount, State state, String comment, String information, int weight) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.filePath = filePath;
        this.price = price;
        this.stockCount = stockCount;
        this.state = state != null ? state.name() : null;
        this.comment = comment;
        this.information = information;
        this.weight = weight;
    }

    public void matchedUntiyPrice(int quantity) {
        this.quantity = quantity;
        unitPrice = price * quantity;
    }

    public void matchedCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }
}
