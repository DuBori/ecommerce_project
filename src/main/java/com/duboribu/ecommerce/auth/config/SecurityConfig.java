package com.duboribu.ecommerce.auth.config;

import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfig config;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final UserLoginSuccessCustomHandler successHandler;
    private final UserLoginFailureCustomHandler failureHandler;

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                //유저관련(회원가입)
                /*.requestMatchers("/codebox/join*", "/codebox/join/mailConfirm", "/codebox/join/validNickName")*/
                //리프레쉬 토큰 관련
                .requestMatchers("/codebox/refreshToken")
                //swagger
                .requestMatchers("/swagger-ui.html/**", "/swagger/**", "/v2/api-docs", "/swagger-resources/**", "/webjars/**")
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**")
                //test
                .requestMatchers( "/test", "/login/oauth2/code/kakao", "/login/oauth2/code/google", "/tokenParsingTest");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        return http.csrf((csrf) -> csrf.disable())
                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 시큐리티가 제공해주는 폼 로그인 UI 사용안함
                // 헤더에 토큰으로 "basic "으로 된 토큰을 사용하는 경우 -> httpBasic() / 사용하지 않으면 "BasicAu~"가 작동안하는데 우리는 JWT 토큰을 사용하니 커스텀해서 등록해주기
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())

                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler))

                // 커스텀 필터 등록
                .addFilter(config.corsFilter())
                .addFilter(new UsernamePasswordAuthenticationCustomFilter(authenticationManager, successHandler, failureHandler))
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider))

                //인증, 권한 api 설정
                .authorizeRequests()

                .and()
                .build();
    }
}