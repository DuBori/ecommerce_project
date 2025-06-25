package com.duboribu.ecommerce.auth.util;

import com.duboribu.ecommerce.auth.JwtException;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.enums.UserType;
import com.duboribu.ecommerce.enums.RoleType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;
    public static final UserDto USER_DTO = new UserDto("test", "1", "정현", RoleType.ROLE_ADMIN, UserType.LOGIN_MEMBER, "login");

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(jwtTokenProvider, "secret", "test-secret-key-test-secret-key-test-secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInMilliseconds", 1000L); // 1초
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshTokenValidityInMilliseconds", 1000L * 60 * 60 * 24 * 14L); // 14일

        jwtTokenProvider.afterPropertiesSet();
    }
    @DisplayName("정상케이스를 위한 초기화 작업")
    void afterinit() {
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInMilliseconds", 1000L * 60 * 60); // 1시간
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshTokenValidityInMilliseconds", 1000L * 60 * 60 * 24 * 14L); // 14일
        jwtTokenProvider.afterPropertiesSet();
    }
    @Test
    @DisplayName("토큰이 만료되었을때 JwtException이 발생해야 한다.")
    @Disabled
    void tokenExpiredThrowChk() {
        String oneSecondToken = jwtTokenProvider.createAccessToken(USER_DTO);
        try {
            // 3초(3000밀리초) 동안 일시 정지
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // InterruptedException이 발생하면 예외 처리
            e.printStackTrace();
        }

        Assertions.assertThrows(JwtException.class, () -> {
            jwtTokenProvider.validateToken(oneSecondToken);
        });
    }

    @Test
    @DisplayName("토큰에 대한 어드민 권한을 잘 가져오는지 확인해 본다.")
    void tokenAuthResChk() {
        afterinit();

        String accessToken = jwtTokenProvider.createAccessToken(USER_DTO);
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        Assertions.assertEquals(authentication.getCredentials().toString(), accessToken);
        Assertions.assertEquals(authentication.getName(), USER_DTO.getUsername());
        Assertions.assertEquals(authentication.getAuthorities().stream().map(it -> it.getAuthority()).collect(Collectors.joining(",")),
                USER_DTO.getRoleType().name());
    }

    @Test
    @DisplayName("토큰 생성 점검이 정상임을 확인한다.")
    void tokenResChk() {
        afterinit();
        String accessToken = jwtTokenProvider.createAccessToken(USER_DTO);

        Assertions.assertTrue(!jwtTokenProvider.isExpired(accessToken));
    }

}