package hello.aiofirstuser.security.handler;

import hello.aiofirstuser.security.auth.PrincipalDetails;
import hello.aiofirstuser.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails)  authentication.getPrincipal();
        Map<String,Object> claims = principalDetails.getMemberDTO().getClaims();

        String accessToken = jwtUtil.generateToken(claims,10);
        String refreshToken = jwtUtil.generateToken(claims,60*24);

        accessToken(response, accessToken);
        refreshToken(response, refreshToken);

        response.setContentType("application/json; charset=UTF-8");

        getRedirectStrategy().sendRedirect(request,response,"http://localhost:9001");
//        getRedirectStrategy().sendRedirect(request,response,"http://13.124.160.75");

    }

    private static void refreshToken(HttpServletResponse response, String refreshToken) {
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(1))
                .path("/")
                .build();

        response.addHeader("Set-Cookie",refreshCookie.toString());
    }

    private static void accessToken(HttpServletResponse response, String accessToken) {
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setPath("/");
        accessCookie.setSecure(false);
        response.addCookie(accessCookie);
        response.addHeader("Authorization", "Bearer " + accessToken);
    }
}
