package hello.aiofirstuser.security.provider.oauth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
    private final Map<String, Object> attributes;
    public KakaoUserInfo(Map<String,Object> attributes){
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        return null;
    }

    public String getNickname(){

        Map<String,Object> properties = (Map<String, Object>) attributes.get("properties");

        return String.valueOf(properties.get("nickname"));
    }

    @Override
    public String getName() {
        return null;
    }
}
