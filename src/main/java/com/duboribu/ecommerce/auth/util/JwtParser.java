package com.duboribu.ecommerce.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.stream.Collectors;

public class JwtParser {
    public static String extractAccessToken(String token) {
        String[] parts = token.split(" ");
        if (parts.length == 2) {
            return parts[1];
        } else {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }
    }

    public static String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(it ->"refreshToken".equals(it.getName()))
                .map(Cookie::getValue)
                .collect(Collectors.joining());
    }
}
