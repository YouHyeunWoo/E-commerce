package com.example.ecommerce.model;

import com.example.ecommerce.domain.MemberEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class Auth {

    //회원가입때 사용할 클래스
    @Data
    @Builder
    public static class Register{
        private String name;
        private String password;
        private String phone;
        private String address;
        private List<String> roles;

        public MemberEntity toEntity(){
            return MemberEntity.builder()
                    .name(this.name)
                    .password(this.password)
                    .phone(this.phone)
                    .address(this.address)
                    .registerDate(LocalDateTime.now())
                    .role(this.roles)
                    .build();
        }
    }

    //로그인때 사용할 클래스
    @Data
    public static class LogIn{
        private String name;
        private String password;
    }
}
