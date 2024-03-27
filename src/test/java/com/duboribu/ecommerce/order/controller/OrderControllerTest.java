package com.duboribu.ecommerce.order.controller;

import com.duboribu.ecommerce.entity.Book;
import com.duboribu.ecommerce.entity.Order;
import com.duboribu.ecommerce.entity.OrderItem;
import com.duboribu.ecommerce.entity.Stock;
import com.duboribu.ecommerce.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.item.dto.CreateStockRequest;
import com.duboribu.ecommerce.item.dto.ResponseItem;
import com.duboribu.ecommerce.item.service.ItemService;
import com.duboribu.ecommerce.repository.BookJpaRepository;
import com.duboribu.ecommerce.repository.OrderJpaRepository;
import com.duboribu.ecommerce.repository.StockJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
class OrderControllerTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private BookJpaRepository bookJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private StockJpaRepository stockJpaRepository;

    @Test
    @Transactional
    public void 주문을생성한다() {
        ResponseItem responseItem = itemService.createItem(new CreateBookRequest("study", "테스터", "출출판사", 500));
        Optional<Book> findBook = bookJpaRepository.findByTitle(responseItem.getTitle());
        if (!findBook.isEmpty()) {
            int stockAdd = itemService.addStock(new CreateStockRequest(findBook.get().getId(), 100));
            System.out.println("재고 설정완료 : " + stockAdd);
            Book book = findBook.get();
            Optional<Stock> findStock = stockJpaRepository.findByItem(book);
            if (findStock.isPresent()) {
                OrderItem orderItem = new OrderItem(book, findStock.get(), 3);
                Order order = new Order();
                order.addOrderItem(orderItem);
                Order saveOrder = orderJpaRepository.save(order);
                System.out.println("orderInfo  :" + saveOrder);
                System.out.println("currentStock  : " + stockJpaRepository.getReferenceById(book.getId()).getCount());
                System.out.println("info : " + saveOrder.getOrderItemList());
                System.out.println("totalPrice : " + saveOrder.getTotalPrice());
            }
        }
    }


}