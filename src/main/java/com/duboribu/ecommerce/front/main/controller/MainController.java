package com.duboribu.ecommerce.front.main.controller;

import com.duboribu.ecommerce.admin.item.dto.SearchItemRequest;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.front.category.service.FoCategoryService;
import com.duboribu.ecommerce.front.item.service.FoItemService;
import com.duboribu.ecommerce.front.main.domain.request.FoMyPageOrderResponse;
import com.duboribu.ecommerce.front.member.service.FoMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final FoItemService foItemService;
    private final FoCategoryService foCategoryService;
    private final FoMemberService foMemberService;
    private final JwtTokenProvider jwtTokenProvider;
    @GetMapping
    public String main(SearchItemRequest request, Model model) {
        model.addAttribute("normalList", foItemService.normalList(request, null));
        model.addAttribute("mainCategoryList", foCategoryService.list("book"));
        return "front/index";
    }

    @GetMapping("/mypage")
    public String myPage(HttpServletRequest request, Model model) {
        String userId = jwtTokenProvider.getUserId(request);
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("회원이 아닙니다");
        }
        model.addAttribute("user", foMemberService.findMember(userId));
        List<FoMyPageOrderResponse> orderListByUser = foMemberService.findOrderListByUser(userId);
        log.info("dddddddd :{}", orderListByUser);
        model.addAttribute("myPageOrderResponse", orderListByUser);
        return "front/mypage";
    }


}
