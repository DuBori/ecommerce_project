package com.duboribu.ecommerce.auth.util;

import com.duboribu.ecommerce.auth.JwtException;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.enums.UserType;
import com.duboribu.ecommerce.enums.RoleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Test
    @Disabled
    public void 토큰이만료확인() {
        String 일초토큰 = jwtTokenProvider.createAccessToken(new UserDto("test", "1", "정현", RoleType.ROLE_ADMIN, UserType.LOGIN_MEMBER, "login"));
        try {
            // 3초(3000밀리초) 동안 일시 정지
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // InterruptedException이 발생하면 예외 처리
            e.printStackTrace();
        }

        Assertions.assertThrows(JwtException.class, () -> {
            jwtTokenProvider.validateToken(일초토큰);
        });
    }
    @Test
    public void 토큰만료시간확인() {
        String 일초토큰 = jwtTokenProvider.createAccessToken(new UserDto("test", "1", "정현", RoleType.ROLE_ADMIN, UserType.LOGIN_MEMBER, "login"));
        Long expiration = jwtTokenProvider.getExpiration(일초토큰);
        System.out.println("expiration = " + expiration);
    }
    
}