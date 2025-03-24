package zoo.insightnote.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import zoo.insightnote.domain.user.dto.request.JoinDto;
import zoo.insightnote.domain.user.dto.PaymentUserInfoResponseDto;

@Tag(name = "USER", description = "유저 관련 API")
public interface UserController {

    @Operation(summary = "비회원 회원가입", description = "이메일 인증 후 비회원 회원가입을 진행합니다.")
    ResponseEntity<?> joinProcess(@Parameter(description = "이름, 이메일 기입")@RequestBody JoinDto joinDto);

    @Operation(summary = "비회원 로그인", description = "이메일 인증 후 비회원 로그인을 진행합니다.")
    ResponseEntity<?> login(@Parameter(description = "이메일, 이메일 기입") @RequestBody JoinDto joinDto);

    @Operation(summary = "토큰 기반 본인 확인", description = "토큰에 저장된 username을 반환합니다.")
    ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "쿠키 기반 토큰을 헤더로 변환", description = "쿠키에 저장된 토큰을 헤더로 변환합니다.")
    ResponseEntity<?> convertTokenToHeader(@Parameter(description = "쿠키에 저장된 토큰") @CookieValue(value = "Authorization", required = false) String token);

    @Operation(summary = "토큰 반환", description = "쿠키에 저장된 토큰을 반환합니다.")
    ResponseEntity<Map<String, String>> getToken(HttpServletRequest request);

    @Operation(summary = "유저 정보 반환", description = "결제 시 참가자 신청 정보에 보여지는 유저 정보를 반환합니다.")
    ResponseEntity<PaymentUserInfoResponseDto> getPaymentUserInfo(@AuthenticationPrincipal UserDetails userDetails);
}
