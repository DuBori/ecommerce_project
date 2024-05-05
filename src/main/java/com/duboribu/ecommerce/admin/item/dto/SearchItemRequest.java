package com.duboribu.ecommerce.admin.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SearchItemRequest {
    private String searchType;
    private String keyword;
    private int page;
    private int pageSize = 10;
    private String category;

    public void matchedCategory(String category) {
        this.category = category;
    }

    public SearchItemRequest(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }
}
