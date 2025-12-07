package com.duboribu.ecommerce.order.service;

import com.duboribu.ecommerce.entity.*;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.enums.OrderState;
import com.duboribu.ecommerce.order.OrderException;
import com.duboribu.ecommerce.order.dto.OrderItemRequestDto;
import com.duboribu.ecommerce.order.dto.OrderRequestDTO;
import com.duboribu.ecommerce.order.orderExceptionType;
import com.duboribu.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class OmsOrderService {
    private final OrderJpaRepository orderJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final StockJpaRepository stockJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;

    private final CartJpaRepository cartJpaRepository;
    @Transactional
    public String create(OrderRequestDTO orderRequestDTO) throws Exception {
        Member member = null;
        Optional<Member> findMember = memberJpaRepository.findById(orderRequestDTO.getUserId());
        if (!findMember.isPresent()) {
            throw new OrderException(orderExceptionType.OMS_ORDER_NON_MEMBER);
        }
        member = findMember.get();
        log.info("member : {}", member);
        Payment payment = null;
        Optional<Payment> findPayment = paymentJpaRepository.findById(orderRequestDTO.getMerchantUid());
        if (findPayment.isPresent()) {
            payment = findPayment.get();
            payment.matchedImpUid(orderRequestDTO.getImpUid());
        }
        log.info("payment : {}", payment);
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemRequestDto orderItemRequestDto : orderRequestDTO.getProductItems()) {
            Item findItem = itemJpaRepository.getReferenceById(orderItemRequestDto.getProductId());
            Stock stock = null;
            Optional<Stock> findStock = stockJpaRepository.findByItem(findItem);
            if (findStock.isPresent()) {
                stock = findStock.get();
            }
            orderItemList.add(new OrderItem(findItem,
                    stock, orderItemRequestDto.getQuantity()));
        }
        Order order = new Order(OrderState.OSI02, member, payment, orderItemList);
        order.getDeliveryList().add(new Delivery(orderRequestDTO));
        Order save = orderJpaRepository.save(order);
        payment.metchedOrder(save);

        if ("cart".equals(orderRequestDTO.getCart())) {
            cartJpaRepository.deleteCartByMemberId(orderRequestDTO.getUserId());
        }

        return payment.getMerchant_uid();
    }
}
