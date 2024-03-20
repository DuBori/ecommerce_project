package com.duboribu.ecommerce.config;

import com.duboribu.ecommerce.auth.JwtExceptionFilter;
import com.duboribu.ecommerce.auth.service.CustomOauth2UserService;
import com.duboribu.ecommerce.auth.util.JwtTokenProvider;
import com.duboribu.ecommerce.enums.RoleType;
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
    private final CustomOauth2UserService customOauth2UserService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtTokenProvider tokenProvider;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf((csrf) -> csrf.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(request -> request.requestMatchers("/swagger-ui/**",
                                "/auth/**", "/join/**","/main", "/swagger-ui.html/**","/login/**",
                                "/swagger/**", "/v2/api-docs", "/swagger-resources/**",
                                "/webjars/**", "/v3/api-docs/**", "/swagger-ui/**",
                                "/fonts/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority(RoleType.ROLE_ADMIN.name())
                        .anyRequest()
                        .authenticated())
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(endPoint -> endPoint.userService(customOauth2UserService))
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler))
                .addFilterBefore(new JwtCustomFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtCustomFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations()
                ).requestMatchers("/fonts/**", "/sass/**", "/Source/**",
                        "/assets/**", "/forms/**"
                ,"/swagger-ui/**","/v3/**");

    }
}