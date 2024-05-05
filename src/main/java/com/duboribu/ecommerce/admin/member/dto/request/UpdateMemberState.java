package com.duboribu.ecommerce.admin.member.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ToString
public class UpdateMemberState {
    private String userId;
    private String state;

    public boolean validate() {
        return !StringUtils.hasText(userId) || !StringUtils.hasText(state);
    }
}
