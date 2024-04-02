package com.duboribu.ecommerce.warehouse.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WmsOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wms_order_id")
    private Long id;
    private String wmsDate; //YYYYMMDD
    private String coCode;
    @OneToMany(mappedBy = "wmsOrder", cascade = CascadeType.ALL)
    private List<WmsOrderItem> wmsOrderItem = new ArrayList<>();

    public WmsOrder(Long id, String wmsDate, String coCode, List<WmsOrderItem> wmsOrderItem) {
        this.id = id;
        this.wmsDate = wmsDate;
        this.coCode = coCode;
        this.wmsOrderItem = wmsOrderItem;
    }
}
