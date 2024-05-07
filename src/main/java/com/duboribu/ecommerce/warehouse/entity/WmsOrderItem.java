package com.duboribu.ecommerce.warehouse.entity;

import com.duboribu.ecommerce.warehouse.enums.WmsOrderState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
/*@NoArgsConstructor(access = AccessLevel.PROTECTED)*/
public class WmsOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wmsId;
    @Column(name = "wms_order_item_id")
    private Long wmsOrderItemId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wms_order_id")
    private WmsOrder wmsOrder;
    @Enumerated(EnumType.STRING)
    private WmsOrderState state;

    public WmsOrderItem(Long id, WmsOrderState state) {
        this.wmsOrderItemId = id;
        this.state = state;
    }

    public void updateOrderState(WmsOrderState newWmsOrderState) {
        state = newWmsOrderState;
    }

    public void matchOrder(WmsOrder wmsOrder) {
        this.wmsOrder = wmsOrder;
        this.state = WmsOrderState.DELIVERY_SET;
    }
}
