package hello.aiofirstuser.config;

import hello.aiofirstuser.security.auth.PrincipalDetails;
import hello.aiofirstuser.security.auth.PrincipalOauth2UserService;
import hello.aiofirstuser.security.filter.JwtCheckFilter;
import hello.aiofirstuser.security.handler.Oauth2SuccessHandler;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.httpBasic(basic -> basic.disable());
        http.formLogin(form -> form.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/loginForm","/","/inquiryanswer/**","/user/logout",
                        "/inquiry/**","/product/**","/api/inquiry/**","/api/inquiryanswer/**", "/api/product/**").permitAll()
                .requestMatchers("/js/**", "/css/**").permitAll()
                .anyRequest().authenticated());
        http.oauth2Login(form -> form
                .loginPage("/loginForm")
                .userInfoEndpoint(endpoint -> endpoint
                        .userService(principalOauth2UserService))
                .successHandler(oauth2SuccessHandler)
        );

        http.addFilterAt(new JwtCheckFilter(jwtUtil,memberService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
