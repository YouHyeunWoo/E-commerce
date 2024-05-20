package com.example.ecommerce.model.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OAuth2UserDto {

    private List<String> role;
    private String name;
    private String socialName;
}
