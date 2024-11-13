package hello.aiofirstuser.security.filter;

import hello.aiofirstuser.dto.member.MemberDTO;
import hello.aiofirstuser.security.auth.PrincipalDetails;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeaderStr = request.getHeader("Authorization");

        String accessToken = null;
        String refreshToken = null;

        accessToken = getAccessToken(authHeaderStr, accessToken);

        if (accessToken == null || accessToken.trim().isEmpty()){
            Cookie[] cookies = request.getCookies();

            if (cookies != null){
                for (Cookie cookie : cookies){
                    if (cookie.getName().equals("accessToken")){
                        accessToken = cookie.getValue();
                    } else if (cookie.getName().equals("refreshToken")){
                        refreshToken = cookie.getValue();
                    }
                }
            }
        }

        if (accessToken != null){
            try {
                Map<String, Object> claims = jwtUtil.validationToken(accessToken);
                setAuthentication((String) claims.get("username"));
            }catch (Exception e){
                if(refreshToken != null){
                    try {
                        Map<String, Object> claims = jwtUtil.validationToken(refreshToken);
                        setAuthentication((String) claims.get("username"));

                        accessToken = jwtUtil.generateToken(claims,10);
                        refreshToken = jwtUtil.generateToken(claims,24*60);

                        setCookie(response, accessToken, refreshToken);
                    }catch (Exception refreshE){
                        cookieReset(response);
                    }
                }else {
                    cookieReset(response);
                }

            }

        }

        filterChain.doFilter(request, response);
    }

    private static void cookieReset(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken",null);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        cookie = new Cookie("refreshToken",null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private static void setCookie(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessCookie =  new Cookie("accessToken", accessToken);
        accessCookie.setPath("/");
        accessCookie.setSecure(false);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(1))
                .path("/")
                .build();

        response.addHeader("Set-Cookie",cookie.toString());
        response.addCookie(accessCookie);
    }

    private void setAuthentication(String username) {
        MemberDTO memberDTO = memberService.getWithRoles(username);

        PrincipalDetails userDetails = new PrincipalDetails(memberDTO);

        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private static String getAccessToken(String authHeaderStr, String accessToken) {
        if(authHeaderStr != null && authHeaderStr.startsWith("Bearer ")){
            accessToken = authHeaderStr.substring(7);
        }
        return accessToken;
    }
}
