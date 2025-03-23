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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import zoo.insightnote.domain.user.service.CustomUserDetailsService;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs") || requestURI.startsWith("/actuator")
            || requestURI.startsWith("/favicon.ico") || requestURI.startsWith("/login") || requestURI.startsWith("/api/v1/sessions")|| requestURI.startsWith("/api/v1/sessions/detailed")|| requestURI.startsWith("/api/v1/speakers")
                || requestURI.startsWith("/api/v1/keywords")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);
        checkAuthorization(request, response, filterChain, token);
        checkToken(request, response, filterChain, token);

        String username = jwtUtil.getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String headerToken = request.getHeader("Authorization");
        if (headerToken != null && headerToken.startsWith("Bearer ")) {
            return headerToken.substring(7); // "Bearer " 이후의 토큰 반환
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
                if (cookie.getName().equals("Authorization")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private void checkToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                            String token) throws IOException, ServletException {
        if (jwtUtil.isExpired(token)) {
            throw new AuthenticationException("token expired") {
            };
        }
    }

    private void checkAuthorization(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                                    String authorization) throws IOException, ServletException {
        if (authorization == null) {
            throw new AuthenticationException("token null") {
            };
        }
    }
}
