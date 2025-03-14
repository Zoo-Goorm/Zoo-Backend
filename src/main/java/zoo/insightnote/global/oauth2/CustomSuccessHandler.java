package zoo.insightnote.global.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import zoo.insightnote.domain.user.dto.CustomOAuth2User;
import zoo.insightnote.global.jwt.JWTUtil;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${FRONT_URL}")
    private String frontUrl;

    private static final long EXPIRATION_TIME = 10 * 60 * 60 * 1000L; // 10시간 (refresh token 적용시 변경 예정)

    private final JWTUtil jwtUtil;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        String token = jwtUtil.createJwt(username, role, EXPIRATION_TIME);

        response.addCookie(createCookie("Authorization", token));

//        ResponseCookie accessTokenCookie = ResponseCookie.from("Authorization", token)
//                .httpOnly(true)   // XSS 공격 방지
//                .secure(true)     // HTTPS 환경에서만 쿠키 전송
//                .sameSite("None") // CORS 환경에서 쿠키 허용
//                .path("/")        // 모든 경로에서 접근 가능
//                .maxAge(60 * 60 * 10) // 10시간 유지
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        response.sendRedirect("https://localhost:3000"); // 추후 프론트 배포 서버로 변경 해야됨.
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        //cookie.setDomain("synapsex.online");
        cookie.setAttribute("SameSite", "None");

        return cookie;
    }
}
