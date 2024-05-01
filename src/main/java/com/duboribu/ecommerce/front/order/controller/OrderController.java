package com.duboribu.ecommerce.front.order.controller;

import com.duboribu.ecommerce.Utils.Validator;
import com.duboribu.ecommerce.auth.JwtException;
import com.duboribu.ecommerce.auth.service.MemberTokenService;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.front.cart.FoCartService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final FoItemService foItemService;
    private final PaymentService paymentService;
    private final MemberTokenService memberTokenService;
    private final FoOrderService foOrderService;
    private final FoCartService foCartService;
    private final JwtTokenProvider jwtTokenProvider;
    @PostMapping("/checkout")
    public String detailOrderPage(@RequestParam(name = "cart", required = false) String cart, CreateOrderRequest createOrderRequest, HttpServletRequest request, Model model) {
        try {
            String accessToken = Validator.getAccessToken(request);
            model.addAttribute("user", memberTokenService.validateToken(accessToken));
        } catch (JwtException e) {
            log.debug("{}", e.getMessage());
        }

        FoOrderResponse foOrderResponse = getFoOrderResponse(cart, createOrderRequest, jwtTokenProvider.getUserId(request));
        if (StringUtils.hasText(cart)) {
            model.addAttribute("cart", "cart");
        }
        model.addAttribute("merchant_uid", paymentService.registerUid(foOrderResponse.getTotalPrice()));
        model.addAttribute("foOrderItemRequest", createOrderRequest);
        model.addAttribute("foOrderItemView", foOrderResponse);
        return "front/checkout";
    }

    private FoOrderResponse getFoOrderResponse(String cart, CreateOrderRequest createOrderRequest, String userId) {
        if (StringUtils.hasText(cart)) {
            FoOrderResponse cartList = foCartService.getCartList(userId);
            if (cartList == null) {
                throw new IllegalArgumentException("카트에 담긴 상품이 없습니다");
            }
            return cartList;
        }
        return foItemService.itemViewResponses(createOrderRequest);
    }

    @GetMapping("/receipt/{id}")
    public String receiptPage(@PathVariable String id, Model model) {
        FoPaymentView byId = foOrderService.findById(id);
        log.info("byId : {}", byId);
        model.addAttribute("foOrderReceipt", foOrderService.findById(id));
        return "front/recipt";
    }
}

