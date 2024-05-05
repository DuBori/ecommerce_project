package com.duboribu.ecommerce.entity.member;

import com.duboribu.ecommerce.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberToken  extends BaseEntity  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_TOKEN_ID")
    private Long id;
    @Column
    private String refreshToken;

    @OneToOne(mappedBy = "memberToken", fetch = FetchType.LAZY)
    private Member member;

    public MemberToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
