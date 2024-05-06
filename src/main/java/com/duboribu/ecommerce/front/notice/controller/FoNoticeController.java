package com.duboribu.ecommerce.front.notice.controller;

import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.enums.NoticeType;
import com.duboribu.ecommerce.front.notice.dto.request.CreateCommentReq;
import com.duboribu.ecommerce.front.notice.service.FoNoticeService;
import com.duboribu.ecommerce.front.qna.dto.request.NoticeRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class FoNoticeController {
    private final JwtTokenProvider jwtTokenProvider;
    private final FoNoticeService foNoticeService;
    @GetMapping("")
    public String notice(Model model) {
        model.addAttribute("list", foNoticeService.list(PageRequest.of(0, 20), new NoticeRequest(NoticeType.NOTICE)));
        return "front/notice";
    }
    @GetMapping("/view/{id}")
    public String noticeView(@PathVariable Long id, Model model) {
        model.addAttribute("notice", foNoticeService.noticeView(id));
        return "front/notice-detials";
    }

    @PostMapping("/commentAdd")
    public ResponseEntity addComment(@RequestBody CreateCommentReq createCommentReq, HttpServletRequest request) {
        String userId = jwtTokenProvider.getUserId(request);
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("로그인 후 이용해주세요.");
        }
        if (foNoticeService.addComment(createCommentReq, userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }

}
