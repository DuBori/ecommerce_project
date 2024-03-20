package com.duboribu.ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
@Slf4j
@Component
@RequiredArgsConstructor
public class IpWhiteListInterceptor implements HandlerInterceptor {
    @Value("${protect.api.ip}")
    private String protectedIp;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String remotAddr = request.getRemoteAddr();
        log.info("ip : {}", remotAddr);
        if (!StringUtils.hasText(protectedIp)) {
            log.info("protectedIp is Empty");
            return true;
        }

        if (!protectedIp.equals(remotAddr)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 제한된 ip입니다.");
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
