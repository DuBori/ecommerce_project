package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.order.dto.OrderRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity implements Serializable {
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

    public Delivery(String address, String sender, String senderPhoneNum, String receiver, String receiverPhoneNum) {
        this.address = address;
        this.sender = sender;
        this.senderPhoneNum = senderPhoneNum;
        this.receiver = receiver;
        this.receiverPhoneNum = receiverPhoneNum;
    }

    public Delivery(OrderRequestDTO orderRequestDTO) {
        this.address = orderRequestDTO.getBuyerAddr();
        this.sender = orderRequestDTO.getUserName();
        this.senderPhoneNum = orderRequestDTO.getPhone();
        this.receiver = orderRequestDTO.getUserName();
        this.receiverPhoneNum = orderRequestDTO.getPhone();
    }
}
