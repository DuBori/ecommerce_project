package com.duboribu.ecommerce.front.member.controller;

import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.front.item.service.FoItemService;
import com.duboribu.ecommerce.front.member.domain.request.LikeReq;
import com.duboribu.ecommerce.front.member.service.MemberLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
@RequestMapping("/member/like")
@RequiredArgsConstructor
public class MemberLikeController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberLikeService memberLikeService;
    private final FoItemService foItemService;
    @GetMapping("")
    public String likePage(Model model, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUserId(request);

        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("회원이 아닙니다.");
        }

        model.addAttribute("FoOrderResponse", foItemService.itemLikeViewResponses(userId));
        return "front/like/view";
    }
    @PostMapping("/click")
    public ResponseEntity likeItem(@RequestBody LikeReq likeReq, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUserId(request);
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("회원이 아닙니다");
        }
        return new ResponseEntity(memberLikeService.clickLike(likeReq, userId), HttpStatus.OK);

    }
}
