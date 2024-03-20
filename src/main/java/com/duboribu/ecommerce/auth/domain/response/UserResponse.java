package com.duboribu.ecommerce.auth.domain.response;

import com.duboribu.ecommerce.auth.domain.UserDto;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserResponse {
    private String id;
    private String name;
    public UserResponse(UserDto userDto) {
        id = userDto.getUsername();
        name = userDto.getName();
    }

    public UserResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
