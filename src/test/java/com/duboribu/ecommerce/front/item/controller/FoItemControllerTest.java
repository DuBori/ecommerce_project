package com.duboribu.ecommerce.front.item.controller;

import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import com.duboribu.ecommerce.front.category.service.FoCategoryService;
import com.duboribu.ecommerce.front.dto.response.FoItemResponse;
import com.duboribu.ecommerce.front.item.service.FoItemService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoItemControllerTest {

    @InjectMocks
    FoItemController foItemController;

    @Mock
    FoItemService foItemService;

    @Mock
    FoCategoryService foCategoryService;

    @Mock
    Model model;

    @Test
    @DisplayName("카테고리 없을 때 리스트 조회 테스트")
    @Disabled
    void NonCate_list() {
        //given
        SearchItemRequest searchItemRequest = new SearchItemRequest(0, 20);
        String category = null;
        Page<FoItemResponse> res = new Page<FoItemResponse>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super FoItemResponse, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<FoItemResponse> getContent() {
                return List.of();
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<FoItemResponse> iterator() {
                return null;
            }
        };

        //when
        when(foItemService.dcList(searchItemRequest, category))
                .thenReturn(res);

        //then
        String returnUrl = foItemController.list(searchItemRequest, model);

        assertEquals("front/shop-grid", returnUrl);
    }

    @Test
    @DisplayName("카테고리 있을 때 리스트 조회 테스트")
    @Disabled
    void UseCate_list() {
        //given
        SearchItemRequest searchItemRequest = new SearchItemRequest(0, 20);
        String category = "history";
        Page<FoItemResponse> res = new Page<FoItemResponse>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super FoItemResponse, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<FoItemResponse> getContent() {
                return List.of();
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<FoItemResponse> iterator() {
                return null;
            }
        };

        //when
        when(foItemService.dcList(searchItemRequest, category))
                .thenReturn(res);

        //then
        String returnUrl = foItemController.list(searchItemRequest, model);

        assertEquals("front/shop-grid", returnUrl);
    }
}