package com.duboribu.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String zip; // 지번
    private String address; // 주소
    private String sender; // 보내는 사람
    private String senderPhoneNum;
    private String receiver; // 수령인
    private String receiverPhoneNum; // 수령인 번호

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
}
