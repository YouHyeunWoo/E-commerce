package com.example.ecommerce.model.auth;

import java.util.Map;

public class GoogleResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {
        //google는 이러한 형식으로 OAuth2Reponse를 준다.
//        {
//            resultcode=00, message=success, id=123123123, name=이름
//        }
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
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
