package com.duboribu.ecommerce.admin.order.service;

import com.duboribu.ecommerce.admin.main.dto.response.BoDashRes;
import com.duboribu.ecommerce.entity.OrderItemJpaRepository;
import com.duboribu.ecommerce.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoOrderService {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;
    @Transactional
    public BoDashRes count() {
        long totalCount = orderJpaRepository.count();
        LocalDateTime startOfYear = LocalDateTime.now().withDayOfYear(1);
        LocalDateTime endOfYear = LocalDateTime.now().withDayOfYear(365);
        int currentCount = orderJpaRepository.countAllByCreatedAtBetween(startOfYear, endOfYear);
        LocalDateTime startOfLastYear = LocalDateTime.now().minusYears(1).withDayOfYear(1);
        LocalDateTime endOfLastYear = LocalDateTime.now().minusYears(1).withDayOfYear(365);
        int lastYearCount = orderJpaRepository.countAllByCreatedAtBetween(startOfLastYear, endOfLastYear);
        return new BoDashRes(totalCount, currentCount, lastYearCount);
    }

    public BoDashRes revenueCount() {
        Long totalCount = orderItemJpaRepository.totalPrice();
        LocalDateTime startOfYear = LocalDateTime.now().withDayOfYear(1);
        LocalDateTime endOfYear = LocalDateTime.now().withDayOfYear(365);
        int currentCount = orderItemJpaRepository.totalPrice(startOfYear, endOfYear);
        LocalDateTime startOfLastYear = LocalDateTime.now().minusYears(1).withDayOfYear(1);
        LocalDateTime endOfLastYear = LocalDateTime.now().minusYears(1).withDayOfYear(365);
        int lastYearCount = orderItemJpaRepository.totalPrice(startOfLastYear, endOfLastYear);
        return new BoDashRes(totalCount, currentCount, lastYearCount);
    }
}
