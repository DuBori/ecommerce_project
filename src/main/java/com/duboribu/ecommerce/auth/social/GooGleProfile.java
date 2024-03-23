package com.duboribu.ecommerce.auth.social;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GooGleProfile {
    private String email;

    public GooGleProfile(String email) {
        this.email = email;
    }
}
