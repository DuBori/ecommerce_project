package com.duboribu.ecommerce.auth.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PublicUserResponse {
    private String id;
    private String name;
    private String role;
    private Long expirationTime;
    public PublicUserResponse(UserResponse userResponse) {
        id = userResponse.getId();
        name = userResponse.getName();
        role = userResponse.getRole();
        expirationTime = userResponse.getExpirationTime();
    }
}
