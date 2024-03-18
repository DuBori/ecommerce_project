package com.duboribu.ecommerce.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@ToString(of = "member")
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member member;
    private Map<String, Object> attributes;


    //일반 로그인 생성자
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    //OAuth 로그인 생성자
    public PrincipalDetails(Member member, Map<String, Object> attributes ) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getRoleType().name()));
    }

    @Override
    public String getPassword() {
        return member.getPwd();
    }
    @Override
    public String getUsername() {
        return member.getId();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String getName() {
        return member.getName();
    }
}