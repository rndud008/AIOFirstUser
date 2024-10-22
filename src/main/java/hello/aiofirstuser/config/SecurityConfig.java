package hello.aiofirstuser.config;

import hello.aiofirstuser.security.auth.PrincipalDetails;
import hello.aiofirstuser.security.auth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.httpBasic(basic -> basic.disable());

        http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll());

        http.formLogin(form -> form
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
        );

        http.oauth2Login(form -> form
                .loginPage("/loginForm")
                .userInfoEndpoint(endpoint -> endpoint
                        .userService(principalOauth2UserService))
        );

        return http.build();
    }
}
