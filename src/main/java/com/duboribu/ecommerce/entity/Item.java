package com.duboribu.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Item extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    private String productCode;
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<Price> prices = new ArrayList<>();
    @OneToOne(mappedBy = "item", fetch = FetchType.LAZY)
    private Stock stock;

    public Item(String productCode, Stock stock) {
        this.productCode = productCode;
        this.stock = stock;
        stock.matchItem(this);
    }

    public void updatePrice(List<Price> prices) {
        this.prices.addAll(prices);
        if (!prices.isEmpty()) {
            prices.stream()
                    .forEach(price -> price.matchItem(this));
        }
    }
}
