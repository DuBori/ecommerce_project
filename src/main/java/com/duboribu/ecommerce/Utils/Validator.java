package com.duboribu.ecommerce.Utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Validator {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (AUTHORIZATION_HEADER.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
