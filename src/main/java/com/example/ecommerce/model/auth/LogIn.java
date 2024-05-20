package com.example.ecommerce.model.auth;

import com.example.ecommerce.domain.MemberEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

//로그인에 사용할 클래스

@Builder
public class LogIn {

    @Getter
    @Builder
    public static class Request{
        private String name;
        private String password;
    }

    @Getter
    @Builder
    public static class Response{
        private String name;
        private Long userId;
        private List<String> roles;

        public static Response fromEntity(MemberEntity memberEntity){
            return Response.builder()
                    .name(memberEntity.getName())
                    .roles(memberEntity.getRole())
                    .userId(memberEntity.getId())
                    .build();
        }
    }
}