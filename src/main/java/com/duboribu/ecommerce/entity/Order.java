package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.enums.OrderState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shop_order")
@Getter
@ToString(of = {"id", "orderItemList"})
/*@NoArgsConstructor(access = AccessLevel.PROTECTED) 테스트를 위해 잠시 주석*/
public class Order extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private OrderState state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItemList = new ArrayList<>();
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<Delivery> deliveryList = new ArrayList<>();

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Payment payment;
    public Order() {
    }

    public Order(OrderState state, Member member, Payment payment, List<OrderItem> orderItemList) {
        this.state = state;
        this.member = member;
        this.payment = payment;
        this.orderItemList = orderItemList;
        for (OrderItem orderItem: orderItemList) {
            orderItem.matchedOrderId(this);
        }
    }

    public int getTotalPrice() {
        if (!getOrderItemList().isEmpty()) {
            return getOrderItemList().stream()
                    .mapToInt(orderItem -> orderItem.getNetPrice() * orderItem.getCount())
                    .sum();
        }
        return 0; // 주문 상품이 없을 경우 0을 반환
    }
}
