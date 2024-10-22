package hello.aiofirstuser.security.auth;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.Role;
import hello.aiofirstuser.dto.MemberDTO;
import hello.aiofirstuser.dto.OAuthResisterMemberDTO;
import hello.aiofirstuser.repository.MemberRepository;
import hello.aiofirstuser.security.provider.oauth.KakaoUserInfo;
import hello.aiofirstuser.security.provider.oauth.OAuth2UserInfo;
import hello.aiofirstuser.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User auth2User = super.loadUser(userRequest);

        log.info("auth2User ={}", auth2User);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = new KakaoUserInfo(auth2User.getAttributes());

        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String nickname = oAuth2UserInfo.getNickname();
        String password = "default";

        MemberDTO memberDTO = memberService.getWithRoles(username);

        if(memberDTO == null){
            OAuthResisterMemberDTO oAuthMemberDTO = OAuthResisterMemberDTO.builder()
                    .username(username)
                    .nickname(nickname)
                    .password(password)
                    .provider(provider)
                    .build();

            memberDTO = memberService.save(oAuthMemberDTO);

        } else {
          log.info("이미 회원가입이 되어 있습니다.");
        }

        return new PrincipalDetails(memberDTO, auth2User.getAttributes());
    }


}
