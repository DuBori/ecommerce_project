package com.duboribu.ecommerce.auth.domain.response;

import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.enums.UserType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserResponse {
    private String id;
    private String name;
    private UserType code;
    private String accessToken;
    private String refreshToken;
    public UserResponse(UserDto userDto) {
        id = userDto.getUsername();
        name = userDto.getName();
        code = userDto.getUserType();
    }

    public UserResponse(UserDto userDto, JwtTokenResponse response) {
        id = userDto.getUsername();
        name = userDto.getName();
        accessToken = response.getAccessToken();
        refreshToken = response.getRefreshToken();
    }

    public UserResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
