package com.duboribu.ecommerce.admin.category.dto;

import com.duboribu.ecommerce.entity.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private Long parentId;
    private String name;
    private String state;
    private List<CategoryResponse> categoryChildList;
    @QueryProjection
    public CategoryResponse(Long id, Long parentId, String name, String state) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.state = state;
    }

    public CategoryResponse(Category save) {
        id = save.getId();
        parentId = save.getParent().getId();
        name = save.getName();
        state = save.getState();
    }

    public void matchedList(List<CategoryResponse> list) {
        categoryChildList = list;
    }
}
