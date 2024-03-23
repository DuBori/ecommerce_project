package com.duboribu.ecommerce.auth.domain;

import com.duboribu.ecommerce.auth.enums.UserType;
import com.duboribu.ecommerce.auth.social.SocialProfile;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.Role;
import com.duboribu.ecommerce.enums.RoleType;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password = "xxx";
    private String name;
    private RoleType roleType;
    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    private UserType userType;

    public UserDto(SocialProfile socialProfile, UserType userType) {
        this.username = socialProfile.getEmail();
        this.name = socialProfile.getName();
        this.userType = userType;

    }

    public Member toEntity(Role role) {
        roleType = role.getRoleType();
        userType = UserType.JOIN_MEMBER;
        return new Member(username, password, name, role);
    }
}
