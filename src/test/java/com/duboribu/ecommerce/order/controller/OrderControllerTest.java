package com.duboribu.ecommerce.order.controller;

import com.duboribu.ecommerce.admin.item.dto.CreateBookRequest;
import com.duboribu.ecommerce.admin.item.dto.CreateStockRequest;
import com.duboribu.ecommerce.admin.item.service.AdminItemService;
import com.duboribu.ecommerce.entity.Book;
import com.duboribu.ecommerce.entity.Order;
import com.duboribu.ecommerce.entity.OrderItem;
import com.duboribu.ecommerce.entity.Stock;
import com.duboribu.ecommerce.front.order.dto.CreateOrderRequest;
import com.duboribu.ecommerce.repository.BookJpaRepository;
import com.duboribu.ecommerce.repository.OrderJpaRepository;
import com.duboribu.ecommerce.repository.StockJpaRepository;
import com.duboribu.ecommerce.warehouse.stock.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
class OrderControllerTest {
    @Autowired
    private AdminItemService adminItemService;
    @Autowired
    private StockService stockService;
    @Autowired
    private BookJpaRepository bookJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private StockJpaRepository stockJpaRepository;

    @BeforeEach
    @Transactional
    public void 상품을생성하고재고를설정한다() {
        adminItemService.createItem(new CreateBookRequest("study", "테스터", "출출판사", 500));
        Optional<Book> findBook = bookJpaRepository.findByTitle("study");
        stockService.addStock(new CreateStockRequest(findBook.get().getId(), 100));
    }
    @Test
    @Transactional
    public void 주문을생성한다() {
        Optional<Book> findBook = bookJpaRepository.findByTitle("study");
        if (!findBook.isEmpty()) {
            Book book = findBook.get();
            Optional<Stock> findStock = stockJpaRepository.findByItem(book);
            if (findStock.isPresent()) {
                OrderItem orderItem = new OrderItem(book, findStock.get(), 3);
                Order order = new Order();
                order.getOrderItemList().add(orderItem);
                Order saveOrder = orderJpaRepository.save(order);

                System.out.println("orderInfo  :" + saveOrder);
                System.out.println("currentStock  : " + stockJpaRepository.getReferenceById(book.getId()).getCount());
                System.out.println("info : " + saveOrder.getOrderItemList());
                System.out.println("totalPrice : " + saveOrder.getTotalPrice());
            }
        }
    }
    @Test
    public void 주문을검증한다() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(5000, 0, null);

        /*Validator.validate(createOrderRequest);*/
    }
}