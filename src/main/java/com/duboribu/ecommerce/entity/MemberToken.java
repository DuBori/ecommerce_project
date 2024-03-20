package com.duboribu.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_TOKEN_ID")
    private Long id;
    @Column
    private String refreshToken;

    @OneToOne(mappedBy = "memberToken")
    private Member member;

    public MemberToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
