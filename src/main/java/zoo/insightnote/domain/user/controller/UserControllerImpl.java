package zoo.insightnote.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.global.jwt.JWTUtil;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final JWTUtil jwtUtil;

    @GetMapping("/auth/token")
    public ResponseEntity<?> convertTokenToHeader(@CookieValue(value = "Authorization", required = false) String token) {
        if (token == null || jwtUtil.isExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired or not found");
        }

        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }
}
