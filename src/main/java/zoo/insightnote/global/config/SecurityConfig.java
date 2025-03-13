package zoo.insightnote.global.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import zoo.insightnote.domain.user.service.CustomOAuth2UserService;
import zoo.insightnote.global.jwt.JWTFilter;
import zoo.insightnote.global.jwt.JWTUtil;
import zoo.insightnote.global.oauth2.CustomSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${FRONT_URL}")
    private String frontUrl;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정 적용
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // CSRF 비활성화
        http.csrf(csrf -> csrf.disable());

        // Form 로그인 방식 비활성화
        http.formLogin(form -> form.disable());

        // HTTP Basic 인증 방식 비활성화
        http.httpBasic(httpBasic -> httpBasic.disable());

        // JWT 필터 추가
        http.addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                        .userService(customOAuth2UserService))
                .successHandler(customSuccessHandler));

        // 경로별 인가 작업
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**", "/user/auth/token", "/actuator/**").permitAll()
                .anyRequest().authenticated()
        );

        // 세션 설정 : STATELESS
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://localhost:3000", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(List.of("Set-Cookie"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}