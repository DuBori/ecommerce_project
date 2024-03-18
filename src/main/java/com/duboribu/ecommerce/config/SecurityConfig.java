package com.duboribu.ecommerce.config;

import com.duboribu.ecommerce.auth.config.CorsConfig;
import com.duboribu.ecommerce.auth.config.JwtAccessDeniedHandler;
import com.duboribu.ecommerce.auth.config.JwtAuthenticationEntryPoint;
import com.duboribu.ecommerce.auth.config.JwtCustomFilter;
import com.duboribu.ecommerce.auth.service.CustomOauth2UserService;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final CustomOauth2UserService customOauth2UserService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final JwtTokenProvider tokenProvider;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf((csrf) -> csrf.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(request -> request.requestMatchers("/swagger-ui/**",
                                "/auth/**", "/join/**", "/main/**", "/admin/**",
                                "/swagger-ui.html/**",
                                "/swagger/**", "/v2/api-docs", "/swagger-resources/**",
                                "/webjars/**", "/v3/api-docs/**", "/swagger-ui/**"
                                , "/fonts/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                /*.addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class)*/
                .addFilterBefore(new JwtCustomFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations()
                ).requestMatchers("/fonts/**", "/sass/**", "/Source/**",
                        "/assets/**", "/forms/**");

    }
}