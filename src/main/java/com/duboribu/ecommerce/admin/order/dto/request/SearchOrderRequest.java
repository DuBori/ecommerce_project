package com.duboribu.ecommerce.admin.order.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchOrderRequest {
    private String searchType;
    private String keyword;
    private int page;
    private int pageSize = 20;

}
