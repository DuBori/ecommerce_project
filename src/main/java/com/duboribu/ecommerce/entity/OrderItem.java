package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.enums.OrderState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"netPrice", "dcPrice", "count"})
public class OrderItem extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private OrderState state;
    private int netPrice;
    private int dcPrice;
    private int count;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    public OrderItem(Item item, Stock stock, int count) {
        this.netPrice = item.getPrice();
        this.dcPrice = 0;
        this.count = count;
        this.item = item;

        if (count > 0) {
            stock.changeStock(-count);
        }
    }

    public void matchedOrderId(Order order) {
        this.order = order;
    }

    public void updateState(OrderState orderState) {
        if (OrderState.OSI02.equals(state) && OrderState.OSI03.equals(orderState)) {
            this.state = orderState;
        }
    }
}
