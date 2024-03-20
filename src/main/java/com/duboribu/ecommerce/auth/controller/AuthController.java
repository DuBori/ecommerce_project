package com.duboribu.ecommerce.auth.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.auth.service.AuthService;
import com.duboribu.ecommerce.auth.service.MemberService;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping("/verify")
    @Operation(summary = "accessToken 재발급", description = "access 토큰을 검증합니다.")
    public ResponseEntity<DefaultResponse> resourceCheck(HttpServletRequest request) {
        Optional<Member> findMember = getMember(request);
        if (findMember.isEmpty()) {
            return new ResponseEntity<>(new DefaultResponse("non member", 202), HttpStatus.OK);
        }
        String refreshToken = findMember.get().getMemberToken().getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return new ResponseEntity<>(new DefaultResponse<>("refresh token expired", 204), HttpStatus.OK);
        }
        return new ResponseEntity<>(new DefaultResponse("complete", 200),settingHeader(jwtTokenProvider.createAccessToken(refreshToken)), HttpStatus.OK);
    }

    @PostMapping("/reissue")
    @Operation(summary = "refresh 토큰 재발급", description = "이름과 비밀번호를 입력해주세요")
    public ResponseEntity<DefaultResponse> createRefreshAuthenticationToken(HttpServletRequest request) {
        Optional<Member> findMember = getMember(request);
        if (findMember.isEmpty()) {
            return new ResponseEntity<>(new DefaultResponse("non member", 202), HttpStatus.OK);
        }
        String refreshToken = findMember.get().getMemberToken().getRefreshToken();
        if (jwtTokenProvider.validateToken(refreshToken)) {
            return new ResponseEntity<>(new DefaultResponse<>("refresh token expired", 204), HttpStatus.OK);
        }
        String token = memberService.issueRefreshToken(findMember.get());
        return new ResponseEntity<>(new DefaultResponse<>("complete", 200), settingHeader(token), HttpStatus.OK);
    }

    private Optional<Member> getMember(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        Optional<Member> findMember = memberService.findById(authentication.getName());
        return findMember;
    }

    private HttpHeaders settingHeader(String token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
