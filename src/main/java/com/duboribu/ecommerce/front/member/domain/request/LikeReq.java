package com.duboribu.ecommerce.front.member.domain.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LikeReq {
    private Long itemId;

    public boolean isEmpty() {
        return itemId == null || itemId == 0;
    }
}
