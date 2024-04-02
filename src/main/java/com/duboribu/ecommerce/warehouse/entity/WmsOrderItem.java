package com.duboribu.ecommerce.warehouse.entity;

import com.duboribu.ecommerce.warehouse.enums.OrderState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WmsOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wms_order_item_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wms_order_id")
    private WmsOrder wmsOrder;
    @Enumerated(EnumType.STRING)
    private OrderState state;

    public WmsOrderItem(Long id, OrderState state) {
        this.id = id;
        this.state = state;
    }
}
