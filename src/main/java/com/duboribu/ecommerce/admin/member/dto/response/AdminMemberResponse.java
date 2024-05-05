package com.duboribu.ecommerce.admin.member.dto.response;

import com.duboribu.ecommerce.enums.RoleType;
import com.duboribu.ecommerce.front.enums.State;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AdminMemberResponse {
    private String userId;
    private String name;
    private String role;
    private String state;
    @QueryProjection
    public AdminMemberResponse(String userId, String name, RoleType role, State state) {
        this.userId = userId;
        this.name = name;
        this.role = role.getDesc();
        this.state = state.getDesc();
    }
}
