package com.example.ecommerce.oauth2;

import com.example.ecommerce.model.auth.CustomOAuth2User;
import com.example.ecommerce.model.auth.Token;
import com.example.ecommerce.security.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //OAuth2User
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        //Name 추출
        String name = customOAuth2User.getName();

        //사용자의 role정보 추출
        Collection<? extends GrantedAuthority> authorities = customOAuth2User.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Token token = this.tokenProvider.generateToken(name, roles);
        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();

        response.addCookie(createCookie("Authorization", accessToken));
        response.addCookie(createCookie("Authorization", refreshToken));
        response.sendRedirect("http://localhost:3000/");
    }

    //쿠키로 토큰 발급
    private Cookie createCookie(String key, String value){

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60); //쿠키만료시간
        //cookie.setSecure(true); //Https환경에서만 동작
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
