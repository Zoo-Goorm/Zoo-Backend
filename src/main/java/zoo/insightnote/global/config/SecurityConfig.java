package zoo.insightnote.global.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import zoo.insightnote.domain.user.service.CustomOAuth2UserService;
import zoo.insightnote.domain.user.service.CustomUserDetailsService;
import zoo.insightnote.global.jwt.GuestAuthenticationProvider;
import zoo.insightnote.global.jwt.GuestLoginFilter;
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
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        List<AuthenticationProvider> providers = List.of(
                new GuestAuthenticationProvider(userDetailsService)
                // 다른 Provider들 추가 가능
        );
        return new ProviderManager(providers);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정 적용
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(List.of(
                        "http://localhost:3000",
                        "https://localhost:3000",
                        "https://www.synapsex.online",
                        "http://www.synapsex.online",
                        "https://api.synapsex.online",
                        "http://dev.synapsex.online",
                        "https://dev.synapsex.online"
                ));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);
                configuration.setExposedHeaders(List.of("Set-Cookie", "Authorization"));

                return configuration;
            }
        }));

        // CSRF 비활성화
        http.csrf(csrf -> csrf.disable());

        // Form 로그인 방식 비활성화
        http.formLogin(form -> form.disable());

        // HTTP Basic 인증 방식 비활성화
        http.httpBasic(httpBasic -> httpBasic.disable());

        // JWT 필터 추가
        http
                .addFilterAt(new GuestLoginFilter("/api/v1/user/login", customAuthenticationManager(), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil, userDetailsService), GuestLoginFilter.class);


        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(
                        (userInfoEndpointConfig) -> userInfoEndpointConfig.userService(customOAuth2UserService))
                .successHandler(customSuccessHandler));

        // 경로별 인가 작업
        http.authorizeHttpRequests(
                auth -> auth
                        //.requestMatchers("/api/v1/user/me").hasRole("USER")
                        //.requestMatchers("/api/v1/user/guest/me").hasRole("GUEST")
                        .requestMatchers(HttpMethod.GET, "/api/v1/sessions").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/sessions/*").permitAll()
                        .requestMatchers(getPermitAllUris()).permitAll() // 모든 http 메소드 로그인 없이 접근 허용하는 경로
                        .anyRequest().authenticated());

        // 세션 설정 : STATELESS
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // 로그인 없이 접근 가능한 경로
    private String[] getPermitAllUris() {
        return new String[] {
                "/",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/user/auth/token",
                "/actuator/**",
                "/api/v1/speakers/**",
                "/api/v1/keywords/**",
                "/api/v1/user/**"
        };
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(List.of("https://localhost:3000", "http://localhost:3000", "https://www.synapsex.online"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        configuration.setMaxAge(3600L);
//        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}