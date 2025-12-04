package com.duboribu.ecommerce.auth.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.domain.response.JwtTokenResponse;
import com.duboribu.ecommerce.auth.domain.response.PublicUserResponse;
import com.duboribu.ecommerce.auth.domain.response.UserResponse;
import com.duboribu.ecommerce.auth.service.AuthService;
import com.duboribu.ecommerce.auth.service.MemberService;
import com.duboribu.ecommerce.auth.service.MemberTokenService;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberTokenService memberTokenService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private MemberJpaRepository memberJpaRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    @DisplayName("기존 사용자 회원가입 시도")
    void old_signUp() {
        // given
        UserDto userDto = new UserDto();
        userDto.setUsername("test");

        // 기존 유저 가입 시도
        Mockito.when(authService.join(ArgumentMatchers.any(UserDto.class)))
                .thenReturn(null);

        // when
        ResponseEntity<DefaultResponse> response = authController.signUp(userDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode()); // or whatever it returns
        assertEquals("이미 회원입니다.", response.getBody().getResMsg());
    }

    @Test
    @DisplayName("신규 회원가입 시도")
    void new_signUp() {
        // given
        UserDto userDto = new UserDto();
        userDto.setUsername("test");

        // 기존 유저 가입 시도
        Mockito.when(authService.join(ArgumentMatchers.any(UserDto.class)))
                .thenReturn(new UserResponse(userDto));

        // when
        ResponseEntity<DefaultResponse> response = authController.signUp(userDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode()); // or whatever it returns
        assertEquals("회원가입 완료", response.getBody().getResMsg());
    }

    @Test
    @DisplayName("기존 어드민 존재")
    void old_adminSignUp() {
        // given
        UserDto userDto = new UserDto();
        userDto.setUsername("admin");

        // when
        Mockito.when(authService.joinAdmin(userDto))
                .thenReturn(null);

        // then
        ResponseEntity<DefaultResponse> defaultResponseResponseEntity = authController.adminSignUp(userDto);
        assertEquals(HttpStatus.OK, defaultResponseResponseEntity.getStatusCode());
        assertEquals("이미 회원입니다.", defaultResponseResponseEntity.getBody().getResMsg());
    }

    @Test
    @DisplayName("아이디 또는 비밀번호 불일치 여부 확인")
    void signInFail() {
        // given
        UserDto userDto = new UserDto();
        userDto.setUsername("test");
        HttpServletResponse httpServletResponse = null;

        Mockito.when(memberService.findById("test"))
                .thenReturn(Optional.of(new Member())); 

        Mockito.when(authService.login(userDto))
                .thenReturn(null);

        ResponseEntity<DefaultResponse> defaultResponseResponseEntity = authController.signIn(userDto, httpServletResponse);
        assertEquals(HttpStatus.OK, defaultResponseResponseEntity.getStatusCode());
        assertEquals("아이디 또는 비밀번호가 잘못되었습니다", defaultResponseResponseEntity.getBody().getResMsg());
    }
    @Test
    @DisplayName("정상 로그인 확인")
    void signInSuccess() {
        // given
        UserDto userDto = new UserDto();
        userDto.setUsername("test");


        Mockito.when(memberService.findById("test"))
                .thenReturn(Optional.of(new Member()));

        Mockito.when(authService.login(userDto))
                .thenReturn(new UserResponse(userDto));

        Mockito.when(authService.login(userDto))
                .thenReturn(new UserResponse(userDto, new JwtTokenResponse("zz","zz1")));



        ResponseEntity<DefaultResponse> defaultResponseResponseEntity = authController.signIn(userDto, httpServletResponse);
        DefaultResponse body = defaultResponseResponseEntity.getBody();
        PublicUserResponse publicUserResponse = (PublicUserResponse) body.getBody();

        assertEquals(HttpStatus.OK, defaultResponseResponseEntity.getStatusCode());
        assertEquals("test", publicUserResponse.getId());
    }

    @Test
    @DisplayName("정상 로그아웃 확인")
    void logOut() {
        //given

        //when

        //then
        ResponseEntity<DefaultResponse> defaultResponseResponseEntity = authController.logOut(httpServletRequest, httpServletResponse);
        assertEquals(HttpStatus.OK, defaultResponseResponseEntity.getStatusCode());
        assertEquals("로그아웃 완료", defaultResponseResponseEntity.getBody().getResMsg());


    }
}