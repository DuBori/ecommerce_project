package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.auth.domain.UserDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_TOKEN_ID")
    private MemberToken memberToken;

    public Member(String id, String pwd, String name) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
    }

    public Member(String id, String pwd, String name, Role role, MemberToken memberToken) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.role = role;
        this.memberToken = memberToken;
    }

    public Member(UserDto userDto) {
        this.id = userDto.getUsername();
        this.pwd = userDto.getPassword();
        this.name = userDto.getName();
    }

    public Member(String id, String pwd, String name, Role role) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.role = role;
    }

    public void updateToken(MemberToken memberToken) {
        this.memberToken = memberToken;
    }
}
