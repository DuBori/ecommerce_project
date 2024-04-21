package com.duboribu.ecommerce.front.order.controller;

import com.duboribu.ecommerce.Utils.Validator;
import com.duboribu.ecommerce.auth.JwtException;
import com.duboribu.ecommerce.auth.service.MemberTokenService;
import com.duboribu.ecommerce.front.item.service.FoItemService;
import com.duboribu.ecommerce.front.order.dto.CreateOrderRequest;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;
import com.duboribu.ecommerce.front.order.dto.FoPaymentView;
import com.duboribu.ecommerce.front.order.service.FoOrderService;
import com.duboribu.ecommerce.front.order.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final FoItemService foItemService;
    private final PaymentService paymentService;
    private final MemberTokenService memberTokenService;
    private final FoOrderService foOrderService;
    @PostMapping("/checkout")
    public String detailOrderPage(HttpServletRequest request, CreateOrderRequest createOrderRequest, Model model) {
        try {
            String accessToken = Validator.getAccessToken(request);
            model.addAttribute("user", memberTokenService.validateToken(accessToken));
        } catch (JwtException e) {
            log.debug("{}", e.getMessage());
        }
        FoOrderResponse foOrderResponse = foItemService.itemViewResponses(createOrderRequest);
        model.addAttribute("foOrderItemView", foOrderResponse);
        model.addAttribute("merchant_uid", paymentService.registerUid(foOrderResponse.getTotalPrice()));
        model.addAttribute("foOrderItemRequest", createOrderRequest);
        return "front/checkout";
    }
    @GetMapping("/receipt/{id}")
    public String receiptPage(@PathVariable String id, Model model) {
        FoPaymentView byId = foOrderService.findById(id);
        log.info("byId : {}", byId);
        model.addAttribute("foOrderReceipt", foOrderService.findById(id));
        return "front/recipt";
    }
}

