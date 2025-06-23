package com.duboribu.ecommerce.admin.order.service;

import com.duboribu.ecommerce.Utils.RestUtil;
import com.duboribu.ecommerce.admin.order.dto.request.UpdateOrderReq;
import com.duboribu.ecommerce.entity.Delivery;
import com.duboribu.ecommerce.entity.Order;
import com.duboribu.ecommerce.entity.OrderItem;
import com.duboribu.ecommerce.enums.OrderState;
import com.duboribu.ecommerce.repository.OrderJpaRepository;
import com.duboribu.ecommerce.warehouse.order.dto.request.CreateDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.response.RegisterResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class BoOrderServiceTest {
    @Autowired
    OrderJpaRepository orderJpaRepository;

    /**
     * 주문 프로세스 진행
     *
     * 발주등록 (수동), 시간배치
     * 발주조회 배치로 출고완료 및 배송완료 시, 주문, DELIVERY 업데이트
     *
     * 주문건 생성 시, 주문 - 주문상품 DELIVERY 생성, WmsOrderState 는 초기값은 없음
     * 발주등록 후, 발주조회하여 정상적으로 조회되면, WmsOrderState Update
     * 
     * 주문 배송완료 후, 7일 후 구매확정으로 자동전환, 구매후기 및 평점 작성이후 전환
     */
    
    // 검증 필요 주문 테스트건 넣어서
    @Test
    @Transactional
    public void 결제완료주문완료단건진행(UpdateOrderReq updateOrderReq) {
        Optional<Order> findOrder = orderJpaRepository.findById(updateOrderReq.getId());
        if (findOrder.isEmpty()) {
            throw new IllegalArgumentException("요청하신 주문이 존재하지 않습니다.");
        }
        Order order = findOrder.get();
        List <CreateDeliveryRequest.OrderInfo> list = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItemList()) {
            list.add(new CreateDeliveryRequest.OrderInfo(order.getId(),
                    orderItem.getId(),
                    orderItem.getCount(),
                    new CreateDeliveryRequest.OrderInfo.DeliveryInfo("테스트", "테스트"))
            );
        }
        // 발주등록 후 발주조회하여 성공한것들 업데이트 성공하지 못하면 업데이트하지않는다.

        ResponseEntity<RegisterResponse> registerResponse = RestUtil.post("http://localhost:8080/wms/order/register",
                new CreateDeliveryRequest(LocalDate.now().format(DateTimeFormatter.ofPattern("YYMMDD")), "SHOP", list),
                null,
                RegisterResponse.class);

        
        for (RegisterResponse.WmsOrderResponse wmsOrderResponse: registerResponse.getBody().getList()){
            order.getDeliveryList().add(new Delivery(wmsOrderResponse.getWmsOrderState(),
                    "테스트",
                    "테스트",
                    "01012341234",
                    "테스트",
                    "01012341234"));    
        }
        
        order.updateOrder(updateOrderReq);
    }

    @Test
    @Transactional
    public void 결제완료주문완료복수건진행배치() {
        List<Order> osiList = orderJpaRepository.findOrderByState(OrderState.OSI02);
        List <CreateDeliveryRequest.OrderInfo> list = new ArrayList<>();
        for (Order order : osiList) {
            for (OrderItem orderItem : order.getOrderItemList()) {
                list.add(new CreateDeliveryRequest.OrderInfo(order.getId(),
                        orderItem.getId(),
                        orderItem.getCount(),
                        new CreateDeliveryRequest.OrderInfo.DeliveryInfo("테스트", "테스트"))
                );
            }
        }

        // 발주등록 후 발주조회하여 성공한것들 업데이트 성공하지 못하면 업데이트하지않는다.

        ResponseEntity<RegisterResponse> registerResponse = RestUtil.post("http://localhost:8080/wms/order/register",
                new CreateDeliveryRequest(LocalDate.now().format(DateTimeFormatter.ofPattern("YYMMDD")), "SHOP", list),
                null,
                RegisterResponse.class);

        for (Order order : osiList) {
            for (RegisterResponse.WmsOrderResponse wmsOrderResponse: registerResponse.getBody().getList()){
                if (order.getId().equals(wmsOrderResponse.getOrderId())) {
                    order.getDeliveryList().add(new Delivery(wmsOrderResponse.getWmsOrderState(),
                            "테스트",
                            "테스트",
                            "01012341234",
                            "테스트",
                            "01012341234"));
                }
            }
        }
    }
}