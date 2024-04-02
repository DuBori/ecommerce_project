package com.duboribu.ecommerce.warehouse.order.service;

import com.duboribu.ecommerce.warehouse.entity.WmsOrder;
import com.duboribu.ecommerce.warehouse.entity.WmsOrderItem;
import com.duboribu.ecommerce.warehouse.enums.OrderState;
import com.duboribu.ecommerce.warehouse.order.dto.request.CreateDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.repository.WmsOrderJpaRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WmsOrderServiceTest {
    @Autowired
    WmsOrderJpaRepository wmsOrderJpaRepository;

    @Test
    @Disabled
    public void 단건발주등록을한다() {
        CreateDeliveryRequest request = new CreateDeliveryRequest("20240402", "TEST",
                Collections.singletonList(new CreateDeliveryRequest.OrderInfo(1L, 1L, 1,
                        new CreateDeliveryRequest.OrderInfo.DeliveryInfo("12345", "테스트"))));
        CreateDeliveryRequest.OrderInfo orderInfo = request.getOrderList().get(0);
        Optional<WmsOrder> findWmsOrder = wmsOrderJpaRepository.findById(orderInfo.getOrderId());
        WmsOrder wmsOrder = orderInfo.toEntity();
        WmsOrder save = wmsOrderJpaRepository.save(wmsOrder);
        assertEquals(orderInfo.getOrderId(), save.getId());
    }

    @Test
    @Transactional
    @Disabled
    public void 복수발주등록을한다() {
        CreateDeliveryRequest.OrderInfo orderInfo1 = new CreateDeliveryRequest.OrderInfo(1L, 1L, 1, new CreateDeliveryRequest.OrderInfo.DeliveryInfo("12345", "테스트"));
        CreateDeliveryRequest.OrderInfo orderInfo2 = new CreateDeliveryRequest.OrderInfo(1L, 2L, 1, new CreateDeliveryRequest.OrderInfo.DeliveryInfo("12345", "테스트"));
        List<CreateDeliveryRequest.OrderInfo> list = new ArrayList<>();
        list.add(orderInfo1);
        list.add(orderInfo2);
        CreateDeliveryRequest request = new CreateDeliveryRequest("20240402", "TEST", list);
        for (CreateDeliveryRequest.OrderInfo orderInfo : request.getOrderList()) {
            Optional<WmsOrder> findWmsOrder = wmsOrderJpaRepository.findById(orderInfo.getOrderId());
            if (findWmsOrder.isPresent()) {
                WmsOrder wmsOrder = findWmsOrder.get();
                wmsOrder.getWmsOrderItem().add(new WmsOrderItem(orderInfo.getOrderItemId(), OrderState.DELIVERY_SET));
            } else {
                WmsOrder wmsOrder = orderInfo.toEntity();
                wmsOrderJpaRepository.save(wmsOrder);
            }

        }
        List<WmsOrderItem> wmsOrderItem = wmsOrderJpaRepository.findById(1L).get().getWmsOrderItem();

        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i).getOrderItemId(), wmsOrderItem.get(i).getId());
        }
    }

    /*@Test
    public void 발주등록건프로세스를진행한다() {
        ProcessDeliveryRequest request = new ProcessDeliveryRequest("DELIVERY_READY", Collections.singletonList(new ProcessDeliveryRequest.CoDeliveryInfo(1L, "TEST")));
        String newOrderStateRequest = request.getNewOrderState();
        OrderState matchState = OrderState.getMatchState(newOrderStateRequest);

        List<ProcessDeliveryRequest.CoDeliveryInfo> orderItemId = request.getOrderItemId();
        List<UpdateWmsOrderResponse> successId = new ArrayList<>();
        List<UpdateWmsOrderResponse> failsId = new ArrayList<>();

        if (!orderItemId.isEmpty()) {
            for (ProcessDeliveryRequest.CoDeliveryInfo info  : orderItemId) {
                UpdateWmsOrderResponse updateWmsOrderResponse = updateWmsOrder(info.getOrderItemId(), info.getCoCode(), matchState);
                String result = updateWmsOrderResponse.getResult();
                if ("success".equals(result)) {
                    successId.add(updateWmsOrderResponse);
                } else {
                    failsId.add(updateWmsOrderResponse);
                }
            }
        }
    }
    private UpdateWmsOrderResponse updateWmsOrder(Long companyOrderId, String companyCode, OrderState newOrderState) {
        // 발주 등록된 주문건 존재하는지 가져오고 이전 프로세스가 진행중인지 확인한다
        // 이전 프로세스가 맞으면 신규 프로세스 상태로 업데이트해준다
        // 실패한다면 wmsOrderLog에 실패한것들 로그로 쌓고 메일로 알려준다. // 이건 생각좀해보자
        // 일단 성공/실패에 따른 객체 만들어서 반환하고 그 그거에따라 발주 프로세스 성공실패 따로 담는다    성공여부/회사주문아이디/신규상태값
        Optional<WmsOrder> findWmsOrder = wmsOrderJpaRepository.findByCoCodeAndWmsOrderItem(companyCode, companyOrderId);
        if (findWmsOrder.isPresent()) {
            WmsOrder wmsOrder = findWmsOrder.get();
            wmsOrder.updateState(newOrderState);
            return new UpdateWmsOrderResponse("success", companyOrderId, newOrderState);
        }
        return new UpdateWmsOrderResponse("fail", companyOrderId, newOrderState);
    }*/

}