package com.duboribu.ecommerce.auth.domain;

import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.Role;
import com.duboribu.ecommerce.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String name;
    private Role role;

    //@Transient
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
        this.role = role;
        return new Member(this);
    }

}
