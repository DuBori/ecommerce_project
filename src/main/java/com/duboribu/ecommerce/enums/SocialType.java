package com.duboribu.ecommerce.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    NAVER("네이버"),
    KAKAO("카카오"),
    GOOGLE("구글"),
    ERROR("에러");
    private final String desc;

    public static SocialType getSite(String site) {
        return Arrays.stream(values())
                .filter(it -> site.equals(it.name().toLowerCase()))
                .findAny()
                .orElse(ERROR);
    }
    
}
