package com.duboribu.ecommerce.admin.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CreateStockRequest {
    private Long id;
    private int count;

    public CreateStockRequest(Long id, int count) {
        this.id = id;
        this.count = count;
    }
}
