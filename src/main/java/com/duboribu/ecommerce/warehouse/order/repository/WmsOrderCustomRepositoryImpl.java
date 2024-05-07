package com.duboribu.ecommerce.warehouse.order.repository;

import com.duboribu.ecommerce.warehouse.order.dto.request.SelectDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.response.QUpdateWmsOrderResponse;
import com.duboribu.ecommerce.warehouse.order.dto.response.UpdateWmsOrderResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

import static com.duboribu.ecommerce.warehouse.entity.QWmsOrder.wmsOrder;
import static com.duboribu.ecommerce.warehouse.entity.QWmsOrderItem.wmsOrderItem;
@Repository
@RequiredArgsConstructor
public class WmsOrderCustomRepositoryImpl implements WmsOrderCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UpdateWmsOrderResponse> getList(SelectDeliveryRequest request, Pageable pageable) {
        if (!StringUtils.hasText(request.getDate())) {
            return Collections.emptyList();
        }
        return jpaQueryFactory.select(new QUpdateWmsOrderResponse(wmsOrder.wmsDate, wmsOrder.coCode, wmsOrderItem.wmsId, wmsOrderItem.state))
                .from(wmsOrder)
                .innerJoin(wmsOrderItem)
                .on(wmsOrder.orderId.eq(wmsOrderItem.wmsOrder.id))
                .where(
                        wmsOrder.wmsDate.eq(request.getDate()),
                        wmsOrder.coCode.eq(request.getCoCode())
                        )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}

