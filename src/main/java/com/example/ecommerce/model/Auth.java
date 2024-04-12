package com.example.ecommerce.model;

import com.example.ecommerce.domain.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
        @NotBlank(message = "전화번호를 입력해 주세요")
        @Size(max = 11, min = 11, message = "전화번호 11자리를 입력해 주세요.")
        private String phone;
        @NotBlank
        private String address;
        @NotBlank
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
