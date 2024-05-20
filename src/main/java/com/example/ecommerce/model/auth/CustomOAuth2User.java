package com.example.ecommerce.model.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2UserDto userDto;

    //받은 데이터 값을 리턴
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    //role값을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDto.getRole().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return userDto.getName();
    }
}
