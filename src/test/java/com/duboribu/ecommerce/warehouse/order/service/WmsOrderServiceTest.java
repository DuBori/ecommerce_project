package com.duboribu.ecommerce.warehouse.order.service;

import com.duboribu.ecommerce.Utils.eamil.EmailService;
import com.duboribu.ecommerce.Utils.eamil.dto.EmailMessage;
import com.duboribu.ecommerce.warehouse.entity.WmsOrder;
import com.duboribu.ecommerce.warehouse.entity.WmsOrderItem;
import com.duboribu.ecommerce.warehouse.enums.WmsOrderState;
import com.duboribu.ecommerce.warehouse.order.dto.request.CreateDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.request.ProcessDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.response.UpdateWmsOrderResponse;
import com.duboribu.ecommerce.warehouse.order.repository.WmsOrderItemJpaRepository;
import com.duboribu.ecommerce.warehouse.order.repository.WmsOrderJpaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WmsOrderServiceTest {
    @Autowired
    WmsOrderJpaRepository wmsOrderJpaRepository;
    @Autowired
    WmsOrderItemJpaRepository wmsOrderItemJpaRepository;

    @Autowired
    EmailService emailService;

    @BeforeAll
    public void init() {
        CreateDeliveryRequest request = new CreateDeliveryRequest("20240402", "TEST",
                Collections.singletonList(new CreateDeliveryRequest.OrderInfo(1L, null, 1,
                        new CreateDeliveryRequest.OrderInfo.DeliveryInfo("12345", "테스트"))));
        CreateDeliveryRequest.OrderInfo orderInfo = request.getOrderList().get(0);
        WmsOrder wmsOrder = orderInfo.toEntity();
        WmsOrder save = wmsOrderJpaRepository.save(wmsOrder);
        assertEquals(orderInfo.getOrderId(), save.getId());
    }

    @Test
    @Transactional
    @Disabled
    public void 단건발주등록을한다() {
        CreateDeliveryRequest request = new CreateDeliveryRequest("20240402", "TEST",
                Collections.singletonList(new CreateDeliveryRequest.OrderInfo(1L, null, 1,
                        new CreateDeliveryRequest.OrderInfo.DeliveryInfo("12345", "테스트"))));
        CreateDeliveryRequest.OrderInfo orderInfo = request.getOrderList().get(0);
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
                wmsOrder.getWmsOrderItem().add(new WmsOrderItem(orderInfo.getOrderItemId(), WmsOrderState.DELIVERY_SET));
            } else {
                WmsOrder wmsOrder = orderInfo.toEntity();
                wmsOrderJpaRepository.save(wmsOrder);
            }

        }
        List<WmsOrderItem> wmsOrderItem = wmsOrderJpaRepository.findById(1L).get().getWmsOrderItem();

        /*for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i).getOrderItemId(), wmsOrderItem.get(i).getId());
        }*/
    }

    @Test
    @Transactional
    public void 발주등록건프로세스를진행한다() {
        CreateDeliveryRequest request2 = new CreateDeliveryRequest("20240402", "TEST",
                Collections.singletonList(new CreateDeliveryRequest.OrderInfo(1L, 2L, 1,
                        new CreateDeliveryRequest.OrderInfo.DeliveryInfo("12345", "테스트"))));
        CreateDeliveryRequest.OrderInfo orderInfo = request2.getOrderList().get(0);

        ProcessDeliveryRequest request = new ProcessDeliveryRequest("DELIVERY_READY", Arrays.asList(new ProcessDeliveryRequest.CoDeliveryInfo(2L, "TEST"),
                new ProcessDeliveryRequest.CoDeliveryInfo(3L, "TEST"),
                new ProcessDeliveryRequest.CoDeliveryInfo(4L, "TEST2")));
        String newOrderStateRequest = request.getNewOrderState();
        WmsOrderState matchState = WmsOrderState.getMatchState(newOrderStateRequest);

        List<ProcessDeliveryRequest.CoDeliveryInfo> orderItemId = request.getOrderItemId();
        if (!orderItemId.isEmpty()) {
            List<UpdateWmsOrderResponse> updateWmsOrderResponses = updateOrderItemState(matchState, orderItemId);

            Map<String, List<UpdateWmsOrderResponse>> collect = updateWmsOrderResponses.stream()
                    .filter(it -> "fail".equals(it.getResult()))
                    .collect(Collectors.groupingBy(UpdateWmsOrderResponse::getCoCode));
            if (!collect.isEmpty()) {
                String 실패건 = collect.values().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
                emailService.sendMailForWms(new EmailMessage("wjdgus8576@naver.com", "test", "실패 건  : " + 실패건));
            }
        }
    }

    private List<UpdateWmsOrderResponse> updateOrderItemState(WmsOrderState matchState, List<ProcessDeliveryRequest.CoDeliveryInfo> orderItemId) {
        List<UpdateWmsOrderResponse> list = new ArrayList<>();
        for (ProcessDeliveryRequest.CoDeliveryInfo info : orderItemId) {
            UpdateWmsOrderResponse updateWmsOrderResponse = updateWmsOrder(info.getCompanyOrderId(), info.getCoCode(), matchState);
            list.add(updateWmsOrderResponse);
        }
        return list;
    }

    private UpdateWmsOrderResponse updateWmsOrder(Long companyOrderId, String companyCode, WmsOrderState newWmsOrderState) {
        // 실패한다면 wmsOrderLog에 실패한것들 로그로 쌓고 메일로 알려준다. // 이건 생각좀해보자
        System.out.println("companyOrderId = " + companyOrderId);
        System.out.println("companyCode = " + companyCode);
        Optional<WmsOrderItem> findWmsOrderItem = wmsOrderItemJpaRepository.findByWmsIdAndWmsOrder_CoCode(companyOrderId, companyCode);
        if (findWmsOrderItem.isPresent()) {
            WmsOrderItem wmsOrderItem = findWmsOrderItem.get();
            wmsOrderItem.updateOrderState(newWmsOrderState);
            return new UpdateWmsOrderResponse("success", companyCode, companyOrderId, newWmsOrderState);
        }
        return new UpdateWmsOrderResponse("fail", companyCode, companyOrderId, newWmsOrderState);
    }

}