package zoo.insightnote.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import zoo.insightnote.domain.user.dto.CustomOAuth2User;
import zoo.insightnote.domain.user.dto.UserDto;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = getAuthorization(request);
        checkAuthorization(request, response, filterChain, authorization);

        String token = authorization;
        checkToken(request, response, filterChain, token);

        UserDto userDto = UserDto.builder()
                .username(jwtUtil.getUsername(token))
                .role(jwtUtil.getRole(token))
                .build();
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
                customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private String getAuthorization(HttpServletRequest request) {
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }
        return authorization;
    }

    private void checkToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                           String token) throws IOException, ServletException {
        if (jwtUtil.isExpired(token)) {
            throw new IllegalArgumentException("token expired");
        }
    }

    private void checkAuthorization(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                                  String authorization) throws IOException, ServletException {
        if (authorization == null) {
            throw new IllegalArgumentException("token null");
        }
    }
}
