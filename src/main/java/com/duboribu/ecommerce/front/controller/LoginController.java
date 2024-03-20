package com.duboribu.ecommerce.front.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.domain.response.UserResponse;
import com.duboribu.ecommerce.front.retrofit2.RestInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import retrofit2.Call;
import retrofit2.Response;

@Slf4j
@Controller(value = "LoginController")
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final RestInterface restInterface;
    @RequestMapping()
    public String loginPage() {
        return "/login/login";
    }
    @PostMapping("/login")
    public String signIn(UserDto userDto) {
        log.info("login userDto : {}", userDto);
        try {
            Call<DefaultResponse<UserResponse>> defaultResponseCall = restInterface.signIn(userDto);
            Response<DefaultResponse<UserResponse>> execute = defaultResponseCall.execute();
            DefaultResponse<UserResponse> body = execute.body();
            if (body.getResCode() == 200) {
                log.info("로그인성공");
                return "redirect:/main";
            }
        } catch (Exception e) {

        }
        return null;
    }

    @RequestMapping("/signup")
    public String signUpPage() {
        return "/login/signup";
    }

    @PostMapping("/signup")
    public String signUp(UserDto userDto) {
        log.info("sign up userDto : {}", userDto);
        try {
            Call<DefaultResponse<UserResponse>> responseBodyCall = restInterface.signUp("application/json", userDto);
            Response<DefaultResponse<UserResponse>> execute = responseBodyCall.execute();
            if (execute.isSuccessful()) {
                DefaultResponse<UserResponse> body = execute.body();
                log.info("{}", body.getResMsg());
                log.info("{}", body.getBody().getId());
                log.info("{}", body.getBody().getName());
                log.info("{}", body.getResCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
