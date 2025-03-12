package zoo.insightnote.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;

@Tag(name = "USER", description = "유저 관련 API")
public interface UserController {

    @Operation(summary = "쿠키 기반 토큰을 헤더로 변환", description = "쿠키에 저장된 토큰을 헤더로 변환합니다.")
    ResponseEntity<?> convertTokenToHeader(@Parameter(description = "헤더에 저장된 토큰") @CookieValue(value = "Authorization", required = false) String token);
}
