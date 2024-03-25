package com.duboribu.ecommerce.auth.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.domain.response.JwtTokenResponse;
import com.duboribu.ecommerce.auth.domain.response.PublicUserResponse;
import com.duboribu.ecommerce.auth.domain.response.UserResponse;
import com.duboribu.ecommerce.auth.service.MemberService;
import com.duboribu.ecommerce.auth.service.MemberTokenService;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.enums.SocialType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@Slf4j
@RestController(value = "LoginAPIController")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final MemberTokenService memberTokenService;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * 일반 유저 회원가입 및 리프레시 토큰 저장
     * 그 후 일반 회원은 다시 로그인을 해야한다.
     * */
    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입 및 리프레시 토큰 발급을 진행합니다.")
    public ResponseEntity<DefaultResponse> signUp(@RequestBody UserDto userDto) {
        UserResponse userResponse = memberService.join(userDto);
        if (userResponse == null) {
            log.error("회원가입실패");
            return new ResponseEntity<>(new DefaultResponse("이미 회원입니다.", DefaultResponse.DUP_MEMBER_ERR), HttpStatus.OK);
        }
        log.info("회원가입성공");
        return new ResponseEntity<>(new DefaultResponse("회원가입 완료", DefaultResponse.SUCCESS), HttpStatus.OK);
    }
    /**
     * 통합 로그인이 아닌, 일반 회원인 경우 진입
     * 너 회원인거 확인했어 -> 토큰 바로 발급하고 셋팅해줄게 -> 메인페이지
     * 리프레시 토큰 만료 확인 후, 발급 필요
     * */
    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "회원 정보가 필요합니다")
    public ResponseEntity<DefaultResponse<PublicUserResponse>> signIn(@RequestBody UserDto userDto, HttpServletResponse response) {
        Optional<Member> findMember = memberService.findById(userDto.getUsername());
        if (findMember.isEmpty()) {
            return new ResponseEntity<>(new DefaultResponse<>("회원의 정보가 존재하지않습니다.",DefaultResponse.NON_MEMBER_ERR), HttpStatus.FOUND);
        }
        UserResponse userResponse = memberService.login(userDto);
        if (userResponse == null) {
            return new ResponseEntity<>(new DefaultResponse<>("아이디 또는 비밀번호가 잘못되었습니다", DefaultResponse.SYSTEM_ERR), HttpStatus.FOUND);
        }
        JwtTokenResponse tokenResponse = userResponse.getTokenResponse();
        createCookie(response, new JwtTokenResponse(tokenResponse.getAccessToken(), userResponse.getTokenResponse().getRefreshToken()));
        return new ResponseEntity<>(new DefaultResponse<>(new PublicUserResponse(userResponse)), HttpStatus.FOUND);
    }
    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public ResponseEntity<DefaultResponse> logOut(HttpServletRequest request,HttpServletResponse response) {
        log.info("로그아웃 실행");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        return new ResponseEntity<>(new DefaultResponse<>("로그아웃 완료", 200), HttpStatus.OK);
    }
    /**
     * 통합 소셜 로그인
     * 신규 유저 강제 회원가입 및 리스폰스
     * 토큰 직접 발급 및 관리 필요
     * */
    @GetMapping("/oauth2/code/{site}")
    @Operation(summary = "통합 소셜 로그인", description = "소셜 로그인 [리다이렉트를 받아 신규  회원 강제가입 및 로그인]")
    public ResponseEntity<DefaultResponse<PublicUserResponse>> SocialSignIn(@PathVariable(name = "site") String site, String code, HttpServletResponse response) {
        log.info("소셜 로그인 접속시도");
        UserResponse userResponse = memberService.saveSocialUser(code, SocialType.getSite(site));
        if (userResponse == null) {
            return new ResponseEntity<>(new DefaultResponse<>("해당 사이트는 제공하지않습니다.", DefaultResponse.SYSTEM_ERR), HttpStatus.FOUND);
        }
        JwtTokenResponse tokenResponse = userResponse.getTokenResponse();
        createCookie(response, new JwtTokenResponse(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken()));
        return new ResponseEntity<>(new DefaultResponse<>(new PublicUserResponse(userResponse)), settingHeader(tokenResponse.getAccessToken()) ,HttpStatus.FOUND);
    }


    /**
     * 토큰을 검증 및 aceessToken 재발급
     * */
    @GetMapping("/verify")
    @Operation(summary = "accessToken 재발급", description = "access 토큰을 검증합니다.")
    public ResponseEntity<DefaultResponse<PublicUserResponse>> resourceCheck(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (AUTHORIZATION_HEADER.equals(cookie.getName())) {
                accessToken = cookie.getValue();
            }
        }
        UserResponse userResponse = memberTokenService.validateToken(accessToken);
        if (userResponse == null) {
            return new ResponseEntity<>(new DefaultResponse<>("토큰없음", DefaultResponse.NON_TOKEN),HttpStatus.OK);
        }
        if (userResponse.isTokenExist()) {
            createCookie(response, userResponse.getTokenResponse());
        }
        return new ResponseEntity<>(new DefaultResponse(new PublicUserResponse(new UserResponse(userResponse.getId(), userResponse.getName()))), HttpStatus.OK);
    }

    /**
     * refresh 토큰 재발급
     * */
    @PostMapping("/reissue")
    @Operation(summary = "refresh 토큰 재발급", description = "이름과 비밀번호를 입력해주세요")
    public ResponseEntity<DefaultResponse> createRefreshAuthenticationToken(HttpServletRequest request) {
        /*Optional<Member> findMember = getMember(request);
        if (findMember.isEmpty()) {
            return new ResponseEntity<>(new DefaultResponse("non member", 202), HttpStatus.OK);
        }
        String refreshToken = findMember.get().getMemberToken().getRefreshToken();
        if (jwtTokenProvider.validateToken(refreshToken)) {
            return new ResponseEntity<>(new DefaultResponse<>("refresh token expired", 204), HttpStatus.OK);
        }
        *//*String token = memberService.issueRefreshToken(findMember.get());*/
        return new ResponseEntity<>(new DefaultResponse<>("complete", 200), HttpStatus.OK);
    }



    private HttpHeaders settingHeader(String token) {
        final HttpHeaders headers = new HttpHeaders();
        if (StringUtils.hasText(token)) {
            headers.setLocation(URI.create("/main"));
            return headers;
        }
        headers.setLocation(URI.create("/login"));
        return headers;
    }

    private void createCookie(HttpServletResponse response, JwtTokenResponse tokenResponse) {
        if (StringUtils.hasText(tokenResponse.getRefreshToken())) {
            Cookie cookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
            cookie.setHttpOnly(true);
            cookie.setMaxAge(30 * 24 * 60 * 60); // 30일
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        if (StringUtils.hasText(tokenResponse.getAccessToken())) {
            Cookie cookie = new Cookie("Authorization", tokenResponse.getAccessToken());
            cookie.setHttpOnly(true);
            cookie.setMaxAge(30 * 60); // 30분
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }
}
