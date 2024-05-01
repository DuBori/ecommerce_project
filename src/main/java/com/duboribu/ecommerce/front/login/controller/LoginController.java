package com.duboribu.ecommerce.front.login.controller;

import com.duboribu.ecommerce.Utils.RestUtil;
import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.auth.domain.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller(value = "LoginController")
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    @RequestMapping()
    public String loginPage() {
        return "login/login";
    }
    @PostMapping("/login")
    public String signIn(UserDto userDto) {
        log.info("login userDto : {}", userDto);
        /*try {
            Call<DefaultResponse<UserResponse>> defaultResponseCall = restInterface.signIn(userDto);
            Response<DefaultResponse<UserResponse>> execute = defaultResponseCall.execute();
            DefaultResponse<UserResponse> body = execute.body();
            if (body.getResCode() == 200) {
                log.info("로그인성공");
                return "redirect:/main";
            }
        } catch (Exception e) {

        }*/
        return null;
    }

    @RequestMapping("/signup")
    public String signUpPage() {
        return "login/signup";
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(UserDto userDto) {
        log.info("회원가입 유저 : {} ", userDto.getUsername());
        try {
            ResponseEntity<DefaultResponse> response = RestUtil.post("http://localhost:8080/auth/sign-up", userDto, null, DefaultResponse.class);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("가입 에러");
        }
    }

}
