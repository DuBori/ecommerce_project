package com.duboribu.ecommerce.front.order.service;

import com.duboribu.ecommerce.entity.Payment;
import com.duboribu.ecommerce.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentJpaRepository paymentJpaRepository;
    @Transactional
    public String registerUid(int totalPrice) {
        String uid = makeUid();
        paymentJpaRepository.save(new Payment(uid, totalPrice));
        return uid;
    }

    private String makeUid() {
        StringBuilder uid = new StringBuilder();
        uid.append("ORD");
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        uid.append(currentDate.format(formatter));
        uid.append("-");
        uid.append(String.format("%08d", paymentJpaRepository.countAllByCreatedAtBetween(LocalDateTime.of(currentDate, LocalTime.MIN), LocalDateTime.of(currentDate, LocalTime.MAX)) + 1));
        return uid.toString();
    }

}
