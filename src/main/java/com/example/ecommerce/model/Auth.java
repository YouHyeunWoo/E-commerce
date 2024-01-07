package com.example.ecommerce.model;

import com.example.ecommerce.domain.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class Auth {

    //회원가입에 사용할 클래스
    @Data
    @Builder
    public static class Register {
        @NotBlank
        private String name;
        @NotBlank
        private String password;
        @NotBlank
        private String phone;
        @NotBlank
        private String address;
        @NotNull
        private List<String> roles;

        public MemberEntity toEntity() {
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

    //로그인에 사용할 클래스
    @Data
    public static class LogIn {
        private String name;
        private String password;
    }

    //회원 탈퇴에 사용할 클래스
    @Data
    public static class Withdrawal {
        private String name;
        private String password;
    }
}
