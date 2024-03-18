package com.duboribu.ecommerce.auth.service;

import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.repository.RoleJpaRepository;
import com.duboribu.ecommerce.entity.Member;
import com.duboribu.ecommerce.entity.PrincipalDetails;
import com.duboribu.ecommerce.entity.Role;
import com.duboribu.ecommerce.enums.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("Oatuh loginUser");
        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attribute = getAttribute(userRequest, provider);
        return getPrincipalDetails(attribute, provider);
    }

    private OAuth2User getPrincipalDetails(Map<String, Object> attribute, String provider) {
        String providerId = getId(attribute, provider);
        String username = new StringBuilder().append(provider)
                .append("_")
                .append(providerId)
                .toString();
        Member member = memberService.findByMemberName(username);

        if (member == null) {
            String password =  encoder.encode("test");
            String email = String.valueOf(attribute.get("email"));
            String name = String.valueOf(attribute.get("name"));
            UserDto userResponse = memberService.join(new UserDto(new Member(username, password, name)));
            return new PrincipalDetails(userResponse.toEntity(roleService.findById(RoleType.ROLE_USER)), attribute);
        }

        return new PrincipalDetails(member, attribute);
    }

    private String getId(Map<String, Object> attribute, String provider) {
        if ("google".equals(provider)) {
            return String.valueOf(attribute.get("sub"));
        }
        return String.valueOf(attribute.get("id"));
    }

    private Map<String, Object> getAttribute(OAuth2UserRequest userRequest, String provider) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        if ("google".equals(provider) || "facebook".equals(provider)) {
            return oAuth2User.getAttributes();
        } else if ("naver".equals(provider)) {
            return (Map) oAuth2User.getAttributes().get("response");
        } else if ("kakao".equals(provider)) {
            return (Map) oAuth2User.getAttributes().get("kakao_account");
        }
        throw new IllegalArgumentException("not support service");
    }
}
