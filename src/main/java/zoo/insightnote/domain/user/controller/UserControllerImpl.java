package zoo.insightnote.domain.user.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.email.dto.request.EmailAuthRequest;
import zoo.insightnote.domain.email.service.EmailVerificationService;
import zoo.insightnote.domain.user.dto.request.JoinRequest;
import zoo.insightnote.domain.user.dto.request.UserInfoRequest;
import zoo.insightnote.domain.user.dto.response.PaymentUserInfoResponseDto;
import zoo.insightnote.domain.user.dto.response.UserInfoResponse;
import zoo.insightnote.domain.user.service.UserService;
import zoo.insightnote.global.jwt.JWTUtil;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

//    @PostMapping("/join")
//    public ResponseEntity<?> joinProcess(@Valid @RequestBody JoinRequest joinRequest) {
//        userService.joinProcess(joinRequest);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody JoinRequest joinRequest) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email-auth")
    public ResponseEntity<?> sendEmailAuthCode(@Valid @RequestBody EmailAuthRequest emailAuthRequest) throws MessagingException {
        emailVerificationService.sendVerificationCode(emailAuthRequest.getEmail());
        return ResponseEntity.ok("인증코드를 전송했습니다");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserInfoResponse userInfoResponse = userService.getUserInfo(userDetails.getUsername());
        return ResponseEntity.ok(userInfoResponse);
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateMyInfo(@RequestBody UserInfoRequest userInfoRequest, @AuthenticationPrincipal UserDetails userDetails) {
        UserInfoResponse userInfoResponse = userService.updateUserInfo(userInfoRequest, userDetails.getUsername());
        return ResponseEntity.ok(userInfoResponse);
    }

//    @GetMapping("/me")
//    public ResponseEntity<?> getMyInfo() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return ResponseEntity.ok(username);
//    }

    @GetMapping("/auth/token")
    public ResponseEntity<?> convertTokenToHeader(
            @CookieValue(value = "Authorization", required = false) String token) {

        if (token == null || jwtUtil.isExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired or not found");
        }
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }

    @GetMapping("/get-token")
    public ResponseEntity<Map<String, String>> getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        Map<String, String> response = new HashMap<>();
        if (token != null) {
            response.put("token", token);
        } else {
            throw new RuntimeException("Token not found");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/userInfo")
    public ResponseEntity<PaymentUserInfoResponseDto> getPaymentUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        PaymentUserInfoResponseDto response = userService.getPaymentUserInfo(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
