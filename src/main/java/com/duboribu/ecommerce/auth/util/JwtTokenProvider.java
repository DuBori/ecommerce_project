package com.duboribu.ecommerce.auth.util;

import com.duboribu.ecommerce.auth.JwtException;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.enums.JwtUserExceptionType;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.entity.member.PrincipalDetails;
import com.duboribu.ecommerce.enums.RoleType;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider implements InitializingBean {
    private final MemberJpaRepository memberJpaRepository;
    private final String AUTHORITY_DELIMITER =",";
    private Key key;
    @Value("${custom.jwt.token.key}")
    private String secret;
    @Value("${custom.jwt.access-token-validity-in-milliseconds}")
    private long accessTokenValidityInMilliseconds;
    @Value("${custom.jwt.refresh-token-validity-in-milliseconds}")
    private long refreshTokenValidityInMilliseconds;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    @Transactional
    // 권한 가져오기
    public Authentication getAuthentication(final String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        if (claims.get("id") == null && claims.get("name") == null && claims.get("role") == null) {
            return null;
        }
        String userId = claims.get("id").toString();
        Optional<Member> findMember = memberJpaRepository.findById(userId);
        if (findMember.isEmpty()) {
            return null;
        }
        UserDetails user = new PrincipalDetails(findMember.get());
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    // 토큰 생성
    public String createAccessToken(final UserDto userDto) {
        if (!StringUtils.hasText(userDto.getLoginType())) {
            return null;
        }
        final LocalDateTime now = LocalDateTime.now();
        return Jwts.builder()
                .setSubject(userDto.getUsername())
                .claim("role", userDto.getRoleType())
                .claim("name", userDto.getName())
                .claim("id", userDto.getUsername())
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(now.plusMinutes(accessTokenValidityInMilliseconds / (60 * 1000))))
                .compact();

    }
    // 리프레시 토큰 생성
    public String createRefreshToken(String userId) {
        final LocalDateTime now = LocalDateTime.now();
        return Jwts.builder()
                .setSubject(userId + "_refresh")
                .claim("id", userId)
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(now.plusMinutes(refreshTokenValidityInMilliseconds / (60 * 1000))))
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
            throw new JwtException(JwtUserExceptionType.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new JwtException(JwtUserExceptionType.UN_SUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new JwtException(JwtUserExceptionType.ILLEGAL_JWT_TOKEN);
        }
    }

    public String createAccessToken(String userId, String userName, RoleType role) {
        final LocalDateTime now = LocalDateTime.now();
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .claim("name", userName)
                .claim("id", userId)
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(now.plusMinutes(accessTokenValidityInMilliseconds / (60 * 1000))))
                .compact();
    }

    //엑세스 토큰의 만료시간
    public Long getExpiration(String accessToken){
        Date expiration = Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }
    public boolean isExpired(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
        Date now = new Date();
        return expiration.before(now);
    }
}

