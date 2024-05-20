package com.example.ecommerce.model.auth;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        //naver는 이러한 형식으로 OAuth2Reponse를 준다.
        //response 안의 Map<String, Object> 값을 추출
//        {
//            resultcode=00, message=success, response={id=123123123, name=이름}
//        }
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
