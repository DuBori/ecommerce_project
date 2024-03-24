package com.duboribu.ecommerce.auth.domain;

import com.duboribu.ecommerce.auth.enums.UserType;
import com.duboribu.ecommerce.auth.social.SocialProfile;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.Role;
import com.duboribu.ecommerce.enums.RoleType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password = "xxx";
    private String name;
    private RoleType roleType = RoleType.ROLE_USER;
    private UserType userType = UserType.LOGIN_MEMBER;
    private String loginType;

    public UserDto(SocialProfile socialProfile, UserType userType) {
        this.username = socialProfile.getEmail();
        this.name = socialProfile.getName();
        this.userType = userType;
        this.loginType = "social";
    }

    public Member toEntity(Role role) {
        roleType = role.getRoleType();
        userType = UserType.JOIN_MEMBER;
        return new Member(username, password, name, role);
    }
}
