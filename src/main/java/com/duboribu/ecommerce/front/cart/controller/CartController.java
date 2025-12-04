package com.duboribu.ecommerce.front.cart.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.front.cart.FoCartService;
import com.duboribu.ecommerce.front.cart.dto.request.CartQuantityReq;
import com.duboribu.ecommerce.front.cart.dto.request.CartsRequest;
import com.duboribu.ecommerce.front.cart.dto.response.CartItemResponse;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/ecommerce/cart")
public class CartController {
    private final FoCartService foCartService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("")
    private String cartPage(HttpServletRequest request, Model model) {
        String userId = jwtTokenProvider.getUserId(request);
        FoOrderResponse cartList = foCartService.getCartList(userId);
        model.addAttribute("foOrderResponse", cartList);
        return "front/shoping-cart";
    }
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody CartsRequest cartRequest, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUserId(request);
        if (StringUtils.hasText(userId)) {
            foCartService.addCategory(userId, cartRequest);
        }
        return ResponseEntity.ok().build();

    }

    @PostMapping("/quantity/{unit}")
    public ResponseEntity<DefaultResponse> updateQuantity(@PathVariable String unit, @RequestBody CartQuantityReq cartQuantityReq, HttpServletRequest request) {
        try {
            CartItemResponse cartItemResponse = foCartService.updateQuantity(unit, cartQuantityReq);
            return new ResponseEntity<>(new DefaultResponse(cartItemResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new DefaultResponse(DefaultResponse.SYSTEM_ERR_MSG, DefaultResponse.SYSTEM_ERR), HttpStatus.OK);
        }
    }

    @PostMapping("/delete/{cartItem}")
    public ResponseEntity<DefaultResponse> updateQuantity(@PathVariable Long cartItem, HttpServletRequest request) {
        String userId = "";
        try {
            userId = jwtTokenProvider.getUserId(request);
        } catch (Exception e) {
            return new ResponseEntity<>(new DefaultResponse("토큰 만료", DefaultResponse.EXPIRED_ACCESS_TOKEN), HttpStatus.OK);
        }

        if (foCartService.deleteCartItem(cartItem)) {
            return new ResponseEntity<>(new DefaultResponse("카트 상품 삭제 완료",DefaultResponse.SUCCESS), HttpStatus.OK);
        }

        return new ResponseEntity<>(new DefaultResponse(DefaultResponse.SYSTEM_ERR_MSG, DefaultResponse.SYSTEM_ERR), HttpStatus.OK);
    }

}
