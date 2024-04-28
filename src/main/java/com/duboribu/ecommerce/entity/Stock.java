package com.duboribu.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int count;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public Stock(int count) {
        this.count = count;
    }

    public Stock(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public void matchItem(Item item) {
        this.item = item;
    }

    public void changeStock(int count) {
        int changeCount = this.count + count;
        if (changeCount < 0) {
            new IllegalArgumentException("재고가 존재하지 않습니다.");
        }
        this.count = changeCount;
    }
}
