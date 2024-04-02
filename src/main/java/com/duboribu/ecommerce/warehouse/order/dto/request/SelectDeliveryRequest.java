package com.duboribu.ecommerce.warehouse.order.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SelectDeliveryRequest {
    private String date; // YYYYMMDD
    private String coCode;
    private int page;

    public SelectDeliveryRequest(String date, String coCode) {
        this.date = date;
        this.coCode = coCode;
    }

    public SelectDeliveryRequest(String date, String coCode, int page) {
        this.date = date;
        this.coCode = coCode;
        this.page = page;
    }
}
