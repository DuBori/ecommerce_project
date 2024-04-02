package com.duboribu.ecommerce.warehouse.order.service;

import com.duboribu.ecommerce.warehouse.entity.WmsOrder;
import com.duboribu.ecommerce.warehouse.entity.WmsOrderItem;
import com.duboribu.ecommerce.warehouse.enums.OrderState;
import com.duboribu.ecommerce.warehouse.order.dto.WmsOrderInfo;
import com.duboribu.ecommerce.warehouse.order.dto.request.CreateDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.request.ProcessDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.request.SelectDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.response.UpdateWmsOrderResponse;
import com.duboribu.ecommerce.warehouse.order.repository.WmsOrderJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WmsOrderService {
    private final WmsOrderJpaRepository wmsOrderJpaRepository;
    @Transactional
    public boolean register(CreateDeliveryRequest request) {
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
        return false;
    }
    @Transactional
    // 발주조회
    public List<WmsOrderInfo> list(SelectDeliveryRequest request) {

        return Collections.emptyList();
    }

    // 발주 등록건 프로세스 진행 내부적진행임
    @Transactional
    public List<WmsOrderInfo> processList(ProcessDeliveryRequest request) {
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
        // 성공 실패건들에 대한 후속처리

        return Collections.emptyList();
    }
    private UpdateWmsOrderResponse updateWmsOrder(Long companyOrderId, String companyCode, OrderState newOrderState) {
        // 발주 등록된 주문건 존재하는지 가져오고 이전 프로세스가 진행중인지 확인한다
        // 이전 프로세스가 맞으면 신규 프로세스 상태로 업데이트해준다
        // 실패한다면 wmsOrderLog에 실패한것들 로그로 쌓고 메일로 알려준다. // 이건 생각좀해보자
        // 일단 성공/실패에 따른 객체 만들어서 반환하고 그 그거에따라 발주 프로세스 성공실패 따로 담는다    성공여부/회사주문아이디/신규상태값
        return new UpdateWmsOrderResponse("success", companyOrderId, newOrderState);
    }

}
