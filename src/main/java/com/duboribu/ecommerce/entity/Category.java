package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.admin.category.dto.CreateCategoryRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Category extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    List<Category> childList = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    List<Item> items = new ArrayList<>();

    public Category(CreateCategoryRequest request) {
        name = request.getName();
        code = request.getCode();
        state = request.getState();
    }

    public void addNewCategory(Category save) {
        childList.add(save);
        save.parent = this;
    }
}
