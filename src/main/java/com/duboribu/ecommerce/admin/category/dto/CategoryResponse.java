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
    private String code;
    private String state;
    private List<CategoryResponse> categoryChildList;
    @QueryProjection
    public CategoryResponse(Long id, Long parentId, String name, String code, String state) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.code = code;
        this.state = state;
    }

    public CategoryResponse(Category save) {
        id = save.getId();
        name = save.getName();
        state = save.getState();
        if (save.getParent() != null) {
            parentId = save.getParent().getId();
        }
    }

    public void matchedList(List<CategoryResponse> list) {
        categoryChildList = list;
    }
}
