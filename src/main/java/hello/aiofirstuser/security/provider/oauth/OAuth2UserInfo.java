package hello.aiofirstuser.security.provider.oauth;

public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getNickname();

    String getName();
}
