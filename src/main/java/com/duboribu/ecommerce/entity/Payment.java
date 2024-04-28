package com.duboribu.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Getter
@ToString(of = {"merchant_uid", "impUid", "totalPrice"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity implements Serializable {
    @Id
    private String merchant_uid;
    private int totalPrice;
    private String impUid;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public Payment(String merchant_uid, int totalPrice) {
        this.merchant_uid = merchant_uid;
        this.totalPrice = totalPrice;
    }

    public void matchedImpUid(String impUid) {
        this.impUid = impUid;
    }

    public void metchedOrder(Order order) {
        this.order = order;
    }
}
