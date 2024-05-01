package com.duboribu.ecommerce.front.member.domain.response;

import com.duboribu.ecommerce.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private String id;
    private String name;

    public MemberResponse(Member member) {
        id = member.getId();
        name = member.getName();
    }
}
