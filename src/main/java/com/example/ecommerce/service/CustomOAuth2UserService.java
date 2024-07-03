package com.example.ecommerce.service;

import com.example.ecommerce.domain.MemberEntity;
import com.example.ecommerce.exception.impl.BadRequestForSocialLoginException;
import com.example.ecommerce.model.auth.*;
import com.example.ecommerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        //registrationId 획득 naver요청인지, 구글 요청인지 확인하기 위한 Id
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            throw new BadRequestForSocialLoginException();
        }

        //로그인 관련 로직
        String socialName = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        //social Name으로 아이디 검색
        MemberEntity memberEntity = this.memberRepository.findBySocialName(socialName);

        //소셜 로그인을 한번도 안했을 경우
        //DB에 저장
        if (memberEntity == null) {
            this.memberRepository.save(MemberEntity.builder()
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .socialName(socialName)
                    .registerDate(LocalDateTime.now())
                    .role(List.of("CLIENT"))
                    .build());

            return new CustomOAuth2User(OAuth2UserDto.builder()
                    .name(oAuth2Response.getName())
                    .socialName(socialName)
                    .role(List.of("CLIENT"))
                    .build());

        } else {   //소셜 로그인을 한번이라도 했었을 경우
            //소셜 로그인 서비스측에서 정보가 변동되었을 수 있기 때문에 로그인시 이메일 업데이트
            memberEntity.setEmail(oAuth2Response.getEmail());

            memberEntity = memberRepository.save(memberEntity);
            Hibernate.initialize(memberEntity.getRole());

            return new CustomOAuth2User(OAuth2UserDto.builder()
                    .role(memberEntity.getRole())
                    .name(memberEntity.getName())
                    .socialName(memberEntity.getSocialName())
                    .build());
        }
    }
}
