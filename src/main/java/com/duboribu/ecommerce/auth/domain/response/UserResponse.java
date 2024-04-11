package com.duboribu.ecommerce.auth.domain.response;

import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.enums.UserType;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
public class UserResponse {
    private String id;
    private String name;
    private UserType code;
    private String role;
    private JwtTokenResponse tokenResponse;
    private Long expirationTime;
    public UserResponse(UserDto userDto) {
        id = userDto.getUsername();
        name = userDto.getName();
        code = userDto.getUserType();
    }

    public UserResponse(UserDto userDto, JwtTokenResponse response) {
        id = userDto.getUsername();
        name = userDto.getName();
        role = userDto.getRoleType().getDesc();
        tokenResponse = response;
    }
    public UserResponse(UserDto userDto, JwtTokenResponse response, Long expirationTime) {
        id = userDto.getUsername();
        name = userDto.getName();
        role = userDto.getRoleType().getDesc();
        tokenResponse = response;
        this.expirationTime = expirationTime;
    }

    public UserResponse(String id, String name, Long expirationTime) {
        this.id = id;
        this.name = name;
        this.expirationTime = expirationTime;
    }

    public boolean isTokenExist() {
        if (tokenResponse == null) {
            return false;
        }
        return StringUtils.hasText(tokenResponse.getAccessToken()) || StringUtils.hasText(tokenResponse.getRefreshToken());
    }
}
