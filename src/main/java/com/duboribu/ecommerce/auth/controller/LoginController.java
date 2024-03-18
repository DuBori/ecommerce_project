package com.duboribu.ecommerce.auth.controller;

import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.service.MemberService;
import com.duboribu.ecommerce.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/join")
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원 정보가 필요합니다")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto) {
        UserDto join = memberService.join(userDto);
        if (join.getUsername() == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "회원 정보가 필요합니다")
    public ResponseEntity<UserDetails> signIn(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(memberService.loadUserByUsername(userDto.getUsername()), HttpStatus.OK);
    }
}
