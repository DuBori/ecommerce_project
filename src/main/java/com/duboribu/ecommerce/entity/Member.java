package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(of = {"id", "name"})
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "member_id")
    private String id;
    @Column
    private String pwd;
    @Column
    private String name;

    @Enumerated
    @Column
    private RoleType role;

    public Member(String id, String pwd, String name) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
    }

    public Member(UserDto userDto) {
        this.id = userDto.getUsername();
        this.pwd = userDto.getPassword();
        this.name = userDto.getName();
        this.role = userDto.getRoleType();

    }
}
