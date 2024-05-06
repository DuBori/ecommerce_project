package com.duboribu.ecommerce.front.qna.controller;

import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.enums.NoticeType;
import com.duboribu.ecommerce.front.notice.service.FoNoticeService;
import com.duboribu.ecommerce.front.qna.dto.request.NoticeRequest;
import com.duboribu.ecommerce.front.qna.dto.request.QnaWriteReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/qna")
@RequiredArgsConstructor
public class FoQnaController {
    private final FoNoticeService foNoticeService;
    private final JwtTokenProvider tokenProvider;
    @GetMapping("")
    public String list(Model model, HttpServletRequest request) {
        model.addAttribute("list", foNoticeService.list(PageRequest.of(0, 20), new NoticeRequest(tokenProvider.getUserId(request) ,NoticeType.QNA)));
        return "front/qna/list";
    }

    @GetMapping("/write")
    public String writePage(Model model, HttpServletRequest request) {
        model.addAttribute("user", tokenProvider.getUserId(request));
        return "front/qna/write";
    }

    @PostMapping("/write")
    public String write(QnaWriteReq qnaWriteReq, Model model, HttpServletRequest request) {
        String userId = tokenProvider.getUserId(request);
        Long id = foNoticeService.writeQna(qnaWriteReq, userId);
        return "redirect:/qna/view/" + id;
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("notice", foNoticeService.noticeView(id));
        return "front/qna/view";
    }
}
