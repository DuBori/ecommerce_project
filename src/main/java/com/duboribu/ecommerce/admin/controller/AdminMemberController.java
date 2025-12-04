package com.duboribu.ecommerce.admin.controller;

import com.duboribu.ecommerce.admin.common.BoSearchCommand;
import com.duboribu.ecommerce.admin.member.dto.request.UpdateMemberState;
import com.duboribu.ecommerce.admin.member.dto.response.AdminMemberResponse;
import com.duboribu.ecommerce.admin.member.service.BoMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class AdminMemberController {
    private final BoMemberService boMemberService;
    @GetMapping("/list")
    public String list(BoSearchCommand search, Model model) {
        Page<AdminMemberResponse> list = boMemberService.list(search);
        model.addAttribute("memberResponse", list);
        return "admin/member/list";
    }

    @GetMapping("/view/{id}")
    public String viewPage(@PathVariable String userId, Model model) {

        model.addAttribute("user", boMemberService.getMemberById(userId));
        return "admin/member/view";
    }

    @PostMapping("/update/state")
    public ResponseEntity updateState(@RequestBody UpdateMemberState updateMemberState) {
        boMemberService.updateMemberState(updateMemberState);
        return ResponseEntity.ok().build();
    }

}
