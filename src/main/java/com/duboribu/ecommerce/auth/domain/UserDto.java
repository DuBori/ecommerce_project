package com.duboribu.ecommerce.auth.domain;

import com.duboribu.ecommerce.auth.JwtException;
import com.duboribu.ecommerce.auth.enums.JwtUserExceptionType;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.Role;
import com.duboribu.ecommerce.enums.RoleType;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password = "test";
    private String name;
    private RoleType roleType;
    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    public UserDto(Member member) {
        this.username = member.getId();
        this.password = member.getPwd();
        this.name = member.getName();
    }

    public UserDto(String id, String name, List<SimpleGrantedAuthority> collect) {
        this.username = id;
        this.name = name;
        this.authorities = collect;
    }
    public Member toEntity(Role role) {
        roleType = role.getRoleType();
        return new Member(username, password, name, role);
    }

    public Authentication toAuthentication() {
        if (!StringUtils.hasText(username)) {
            throw new JwtException(JwtUserExceptionType.REQUEST_EMPTY_NAME);
        }
        if (!StringUtils.hasText(password)) {
            throw new JwtException(JwtUserExceptionType.REQUEST_EMPTY_PWD);
        }
        return new UsernamePasswordAuthenticationToken(name, password);
    }

}
