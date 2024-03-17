package com.duboribu.ecommerce.config;

import com.duboribu.ecommerce.auth.config.*;
import com.duboribu.ecommerce.auth.service.CustomOauth2UserService;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOauth2UserService customOauth2UserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
               /* //유저관련(회원가입)
                .requestMatchers("/codebox/join*", "/codebox/join/mailConfirm", "/codebox/join/validNickName")*/
                //리프레쉬 토큰 관련
                .requestMatchers("/codebox/refreshToken")
                .requestMatchers("/auth/*")
                //swagger
                .requestMatchers("/swagger-ui.html/**", "/swagger/**", "/v2/api-docs", "/swagger-resources/**", "/webjars/**")
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**");
                //test
                /*.requestMatchers( "/test", "/login/oauth2/code/kakao", "/login/oauth2/code/google", "/tokenParsingTest");*/
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf((csrf) -> csrf.disable())
                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(request -> request.requestMatchers("/swagger-ui/**", "/auth/**", "/join/**").permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(form -> form.loginProcessingUrl("/join/sign-in")
                        .defaultSuccessUrl("/swagger-ui/index.html"))
                .oauth2Login(oauth -> oauth.defaultSuccessUrl("/swagger-ui/index.html")
                        .failureUrl("/")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOauth2UserService)))
                .build();
    }
}