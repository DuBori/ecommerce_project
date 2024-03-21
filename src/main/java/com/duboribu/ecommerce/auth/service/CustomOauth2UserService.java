package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.domain.request.OAuth2Attribute;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.enums.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberService memberService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("oatuh 진입=====");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체를 만든다.
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Map<String, Object> oAuthMap = oAuth2Attribute.convertToMap();
        String id = (String) oAuthMap.get("email");
        log.info("oauth id : {}", id);
        Optional<Member> findMember = memberService.findById(id);
        // 회원의 권한(회원이 존재하지 않으므로 기본권한인 ROLE_USER를 넣어준다), 회원속성, 속성이름을 이용해 DefaultOAuth2User 객체를 생성해 반환한다.
        if(findMember.isEmpty()) {
            oAuthMap.put("exist", false);
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())),
                    oAuthMap, "email");
        }
        // 기존 유저
        oAuthMap.put("exist", true);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(findMember.get().getRole().getRoleType().name())),
                oAuthMap, "email");
    }

}
