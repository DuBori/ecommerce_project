package com.duboribu.ecommerce.warehouse.order.repository;

import com.duboribu.ecommerce.warehouse.order.dto.request.SelectDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.response.UpdateWmsOrderResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WmsOrderCustomRepository {
    List<UpdateWmsOrderResponse> getList(SelectDeliveryRequest request, Pageable pageable);
}
