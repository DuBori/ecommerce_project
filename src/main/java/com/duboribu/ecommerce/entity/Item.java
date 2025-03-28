package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.admin.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.front.enums.State;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Item extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String productCode;
    private int price;
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<Price> prices = new ArrayList<>(); // 할인
    @OneToOne(mappedBy = "item", fetch = FetchType.LAZY)
    private Stock stock;
    private String comment;
    private String information;
    private int weight;
    private String filePath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Enumerated(EnumType.STRING)
    private State state;

    public Item(String productCode, Stock stock) {
        this.productCode = productCode;
        this.stock = stock;
        stock.matchItem(this);
    }

    public Item(CreateBookRequest request, Category category) {
        this.category = category;
        this.price = request.getPrice();
        this.comment = request.getComment();
        this.information = request.getInformation();
        this.weight = request.getWeight();
        this.filePath = request.getFilePath();
        this.state = State.getmatcheState(request.getState());
    }

    public void updatePrice(List<Price> prices) {
        this.prices.addAll(prices);
        if (!prices.isEmpty()) {
            prices.stream()
                    .forEach(price -> price.matchItem(this));
        }
    }

    protected void updateItemInfo(String filePath, int price, String comment, String information, int weight) {
        this.filePath = filePath;
        this.price = price;
        this.comment = comment;
        this.information = information;
        this.weight = weight;
    }
}
