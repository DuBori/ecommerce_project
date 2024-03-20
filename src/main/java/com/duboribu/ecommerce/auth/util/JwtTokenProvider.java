package com.duboribu.ecommerce.auth.util;

import com.duboribu.ecommerce.auth.JwtException;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.enums.JwtUserExceptionType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider implements InitializingBean {
    private final String AUTHORITIES_KEY ="auth";
    private final String AUTHORITY_DELIMITER =",";

    private Key key;
    @Value("${custom.jwt.token.key}")
    private String secret;
    @Value("${custom.jwt.access-token-validity-in-milliseconds}")
    private int accessTokenValidityInMilliseconds;
    @Value("${custom.jwt.refresh-token-validity-in-milliseconds}")
    private int refreshTokenValidityInMilliseconds;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 권한 가져오기
    public Authentication getAuthentication(final String token) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        List<SimpleGrantedAuthority> collect = Arrays.stream(body.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UserDto user = new UserDto(body.getId(), body.getSubject(), collect);
        return new UsernamePasswordAuthenticationToken(user, token, collect);
    }

    // 토큰 생성
    public String createAccessToken(final Authentication authentication) {
        final LocalDateTime now = LocalDateTime.now();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, toString(authentication.getAuthorities()))
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(now.plusMinutes(accessTokenValidityInMilliseconds)))
                .compact();
    }
    // 리프레시 토큰 생성
    public String createRefreshToken() {
        final LocalDateTime now = LocalDateTime.now();
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(now.plusMinutes(refreshTokenValidityInMilliseconds)))
                .compact();
    }
    
    // 검증
    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException(JwtUserExceptionType.WRONG_JWT_SIGN);
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            throw new JwtException(JwtUserExceptionType.UN_SUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new JwtException(JwtUserExceptionType.ILLEGAL_JWT_TOKEN);
        }
    }

    private String toString(final Collection<? extends GrantedAuthority> grantedAuthorities) {
        return grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(AUTHORITY_DELIMITER));
    }

    public String createAccessToken(String refreshToken) {
        Claims claims = getClaims(refreshToken);
        final LocalDateTime now = LocalDateTime.now();
        return Jwts.builder()
                .setSubject(claims.getSubject())
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(now.plusMinutes(accessTokenValidityInMilliseconds)))
                .compact();
    }

    private Claims getClaims(String refreshToken) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(refreshToken)
                .getBody();
    }
}

