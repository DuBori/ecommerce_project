package com.duboribu.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value;
    private int salePercent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public Price(BigDecimal value, Item item) {
        this.value = value;
        this.item = item;
        item.getPrices().add(this);
    }

    public Price(BigDecimal value, LocalDateTime startDate, LocalDateTime endDate, Item item) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.item = item;
    }

    public void matchItem(Item item) {
        this.item = item;
    }

    public void updatePrice(BigDecimal newPrice) {
        this.value = newPrice;
    }
}
