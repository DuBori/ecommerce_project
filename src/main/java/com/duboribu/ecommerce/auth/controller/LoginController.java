package com.duboribu.ecommerce.auth.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.domain.response.UserResponse;
import com.duboribu.ecommerce.auth.service.AuthService;
import com.duboribu.ecommerce.auth.service.MemberService;
import com.duboribu.ecommerce.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController(value = "LoginAPIController")
@RequestMapping("/join")
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;
    private final AuthService authService;

    /**
     * 회원가입 및 리프레시 토큰 저장
     * 그 후 회원은 다시 로그인을 해야한다.
     * 아이디와 이름을 건내준다. -> 너 회원가입 성공했어, 로그인 다시해-> FO: 로그인페이지 
     * */
    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입 및 리프레시 토큰 발급을 진행합니다.")
    public ResponseEntity<DefaultResponse<UserResponse>> signUp(@RequestBody UserDto userDto) {
        log.info("회원가입진행중");
        UserResponse userResponse = memberService.join(userDto);
        if (userResponse == null) {
            log.info("회원가입진행실패");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new DefaultResponse(userResponse), HttpStatus.OK);
    }

    /**
     * 통합 로그인이 아닌, 회원인 경우 진입한다.
     * 너 회원인거 확인했어 -> 토큰 바로 발급하고 셋팅해줄게 -> 메인페이지
     * */
    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "회원 정보가 필요합니다")
    public ResponseEntity<DefaultResponse<UserResponse>> signIn(@RequestBody UserDto userDto) {
        Optional<Member> findMember = memberService.findById(userDto.getUsername());
        if (findMember.isEmpty()) {
            return new ResponseEntity<>(new DefaultResponse<>("회원의 정보가 존재하지않습니다.",201), HttpStatus.OK);
        }
        Member member = findMember.get();
        String token = authService.createToken(findMember.get());
        return new ResponseEntity<>(new DefaultResponse<>(new UserResponse(member.getId(), member.getName())), settingHeader(token), HttpStatus.OK);
    }

    private HttpHeaders settingHeader(String token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
