package com.duboribu.ecommerce.auth.domain.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PublicUserResponse {
    private String id;
    private String name;
    public PublicUserResponse(UserResponse userResponse) {
        id = userResponse.getId();
        name = userResponse.getName();;
    }
}
