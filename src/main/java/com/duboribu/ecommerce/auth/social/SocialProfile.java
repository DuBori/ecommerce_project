package com.duboribu.ecommerce.auth.social;

import com.duboribu.ecommerce.auth.social.kakao.dto.response.KakaoProfile;
import com.duboribu.ecommerce.auth.social.naver.response.NaverProfile;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SocialProfile {
    private String email;
    private String name;


    public SocialProfile(NaverProfile naverProcess) {
        this.email = naverProcess.getResponse().getEmail();
        this.name = naverProcess.getResponse().getName();
    }

    public SocialProfile(KakaoProfile kakaoProcess) {
        email = "kakao_"+ kakaoProcess.getId();
        name = kakaoProcess.getKakao_account().getProfile().getNickname();
    }

    public SocialProfile(GooGleProfile googleProcess) {
        String email = googleProcess.getEmail();
        this.email = email;
        name =  googleProcess.getEmail().substring(0, email.lastIndexOf("@"));
    }
}
