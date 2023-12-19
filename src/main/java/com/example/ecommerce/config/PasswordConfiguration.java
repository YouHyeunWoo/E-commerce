package com.example.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//PasswordConfiguration과 SecurityConfiguration은 둘 다 Spring security로 생각하여
//하나의 클래스로 합쳐보았다 >> 순환참조가 발생 >>  따로 분리
@Configuration
public class PasswordConfiguration {
    
    @Bean //패스워드 인코딩을 위한 메서드를 빈으로 등록
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
