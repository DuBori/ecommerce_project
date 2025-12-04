package com.duboribu.ecommerce.admin.controller;

import com.duboribu.ecommerce.admin.category.dto.CategoryResponse;
import com.duboribu.ecommerce.admin.category.dto.CreateCategoryRequest;
import com.duboribu.ecommerce.admin.category.dto.SearchCategory;
import com.duboribu.ecommerce.admin.category.service.AdminCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/category")
public class CategoryController {
    private final AdminCategoryService adminCategoryService;
    @GetMapping("")
    public String list(SearchCategory searchCategory, Model model) {
        model.addAttribute("list", adminCategoryService.list(searchCategory));
        return "admin/category/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("category", adminCategoryService.findById(id));
        return "admin/category/view";
    }

    @GetMapping("/create")
    public String createPage() {
        return "admin/category/create";
    }

    @PostMapping("/create")
    public String create(CreateCategoryRequest request) {
        CategoryResponse categoryResponse = adminCategoryService.create(request);
        return "redirect:/admin/category/view/"+ categoryResponse.getId();
    }

    @PostMapping("/delete")
    public String delete(Long id) {
        adminCategoryService.delete(id);
        return "redirect:/admin/category";
    }

}
