package com.duboribu.ecommerce.entity.member;

import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.entity.BaseEntity;
import com.duboribu.ecommerce.front.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Getter
@ToString(of = {"id", "name"})
@NoArgsConstructor
public class Member extends BaseEntity implements Serializable {
    @Id
    @Column(name = "member_id")
    private String id;
    @Column
    private String pwd;
    @Column
    private String name;
    @Enumerated(EnumType.STRING)
    private State state;

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
        this.state = State.Y;
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
        this.state = State.Y;
    }

    public void updateToken(MemberToken memberToken) {
        this.memberToken = memberToken;
    }

    public UserDto getMemberDto() {
        return new UserDto(id, null, name, role.getRoleType(), null, null);
    }

    public void updateState(String state) {
        this.state = State.getmatcheState(state);
    }
}
