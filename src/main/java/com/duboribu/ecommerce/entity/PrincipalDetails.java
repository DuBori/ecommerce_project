package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.auth.domain.response.MemberResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@ToString
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
        return null;
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
        return null;
    }
}