package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.enums.OrderState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"netPrice", "dcPrice", "count"})
public class OrderItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private OrderState state;
    private int netPrice;
    private int dcPrice;
    private int count;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name ="item_id")
    private Item item;
    public OrderItem(Item item, Stock stock, int count) {
        this.netPrice = item.getPrices()
                .get(0)
                .getValue()
                .intValue();
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
}
