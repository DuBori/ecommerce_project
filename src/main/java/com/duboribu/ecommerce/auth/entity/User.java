package com.duboribu.ecommerce.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    //@Transient
    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();
}
