package com.duboribu.ecommerce.warehouse.order.service;

import com.duboribu.ecommerce.warehouse.entity.WmsOrder;
import com.duboribu.ecommerce.warehouse.entity.WmsOrderItem;
import com.duboribu.ecommerce.warehouse.enums.OrderState;
import com.duboribu.ecommerce.warehouse.order.dto.request.CreateDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.request.ProcessDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.request.SelectDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.response.UpdateWmsOrderResponse;
import com.duboribu.ecommerce.warehouse.order.repository.WmsOrderCustomRepository;
import com.duboribu.ecommerce.warehouse.order.repository.WmsOrderItemJpaRepository;
import com.duboribu.ecommerce.warehouse.order.repository.WmsOrderJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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
    private final WmsOrderItemJpaRepository wmsOrderItemJpaRepository;
    private final WmsOrderCustomRepository wmsOrderCustomRepository;
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
    public List<UpdateWmsOrderResponse> list(SelectDeliveryRequest request) {
        return wmsOrderCustomRepository.getList(request, PageRequest.of(request.getPage(), 0));
    }

    // 발주 등록건 프로세스 진행 내부적진행임
    @Transactional
    public List<UpdateWmsOrderResponse> updateOrderStates(ProcessDeliveryRequest request) {
        String newOrderStateRequest = request.getNewOrderState();
        OrderState matchState = OrderState.getMatchState(newOrderStateRequest);
        List<ProcessDeliveryRequest.CoDeliveryInfo> orderItemId = request.getOrderItemId();
        if (!orderItemId.isEmpty()) {
            return updateOrderItemState(matchState, orderItemId);
        }
        return Collections.emptyList();
    }

    private List<UpdateWmsOrderResponse> updateOrderItemState(OrderState matchState, List<ProcessDeliveryRequest.CoDeliveryInfo> orderItemId) {
        List<UpdateWmsOrderResponse> list = new ArrayList<>();
        for (ProcessDeliveryRequest.CoDeliveryInfo info  : orderItemId) {
            UpdateWmsOrderResponse updateWmsOrderResponse = updateWmsOrder(info.getOrderItemId(), info.getCoCode(), matchState);
            list.add(updateWmsOrderResponse);
        }
        return list;
    }

    private UpdateWmsOrderResponse updateWmsOrder(Long companyOrderId, String companyCode, OrderState newOrderState) {
        // 실패한다면 wmsOrderLog에 실패한것들 로그로 쌓고 메일로 알려준다. // 이건 생각좀해보자
        Optional<WmsOrderItem> findWmsOrderItem = wmsOrderItemJpaRepository.findByIdAndWmsOrder_CoCode(companyOrderId, companyCode);
        if (findWmsOrderItem.isPresent()) {
            WmsOrderItem wmsOrderItem = findWmsOrderItem.get();
            wmsOrderItem.updateOrderState(newOrderState);
            return new UpdateWmsOrderResponse("success", companyOrderId, newOrderState);
        }
        return new UpdateWmsOrderResponse("fail", companyOrderId, newOrderState);
    }

}
