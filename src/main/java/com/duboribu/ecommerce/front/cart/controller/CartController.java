package com.duboribu.ecommerce.front.cart.controller;

import com.duboribu.ecommerce.Utils.Validator;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.front.cart.FoCartService;
import com.duboribu.ecommerce.front.cart.dto.CartsRequest;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final FoCartService foCartService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("")
    private String cartPage(HttpServletRequest request, Model model) {
        String userId = getUserId(request);
        FoOrderResponse cartList = foCartService.getCartList(userId);
        log.info("cartList : {}", cartList);
        model.addAttribute("foOrderResponse", cartList);
        return "front/shoping-cart";
    }
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody CartsRequest cartRequest, HttpServletRequest request) {
        String userId = getUserId(request);
        if (StringUtils.hasText(userId)) {
            foCartService.addCategory(userId, cartRequest);
        }
        return ResponseEntity.ok().build();

    }
    private String getUserId(HttpServletRequest request) {
        String accessToken = Validator.getAccessToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }
}
