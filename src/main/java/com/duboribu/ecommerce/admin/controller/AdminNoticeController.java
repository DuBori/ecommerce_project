package com.duboribu.ecommerce.admin.controller;

import com.duboribu.ecommerce.admin.notice.dto.request.BoNoticeReq;
import com.duboribu.ecommerce.admin.notice.dto.response.BoNoticeRes;
import com.duboribu.ecommerce.admin.notice.service.AdminNoticeService;
import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.enums.NoticeType;
import com.duboribu.ecommerce.front.enums.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/admin/notice")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    @GetMapping("")
    public String list(@PageableDefault(size = 10) Pageable pageable,
                       @RequestParam(required = false) String searchType,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        Page<BoNoticeRes> list = adminNoticeService.findNoticeList(pageable, searchType, keyword);
        model.addAttribute("list", list);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("noticeTypes", NoticeType.values());
        model.addAttribute("states", State.values());
        return "admin/notice/list";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("noticeTypes", NoticeType.values());
        model.addAttribute("states", State.values());
        return "admin/notice/create";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        BoNoticeRes notice = adminNoticeService.findNoticeById(id);
        model.addAttribute("notice", notice);
        model.addAttribute("noticeTypes", NoticeType.values());
        model.addAttribute("states", State.values());
        return "admin/notice/view";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<DefaultResponse<BoNoticeRes>> create(@RequestBody BoNoticeReq boNoticeReq) {
        BoNoticeRes result = adminNoticeService.save(boNoticeReq);
        return new ResponseEntity<>(new DefaultResponse<>(result), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<DefaultResponse<BoNoticeRes>> update(@PathVariable Long id, @RequestBody BoNoticeReq boNoticeReq) {
        boNoticeReq.setId(id);
        BoNoticeRes result = adminNoticeService.update(boNoticeReq);
        return new ResponseEntity<>(new DefaultResponse<>(result), HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<DefaultResponse<String>> delete(@PathVariable Long id) {
        adminNoticeService.delete(id);
        return new ResponseEntity<>(new DefaultResponse<>("삭제되었습니다."), HttpStatus.OK);
    }
}
