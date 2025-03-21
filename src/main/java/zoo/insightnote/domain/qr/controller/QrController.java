package zoo.insightnote.domain.qr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zoo.insightnote.domain.qr.dto.QrCheckDto;

@Tag(name = "QR", description = "QR 관련 API")
@RequestMapping("/api/v1/QR")
public interface QrController {

    @Operation(
            summary = "QR 생성 API",
            description = """
                    이벤트, 세션 입장 QR을 생성합니다.
                    - **이벤트 QR 생성**: `qrType = event`로 설정하면 해당 아이디의 이벤트 QR이 생성됩니다.
                    - **세션 QR 생성**: `qrType = session`으로 설정하면 해당 아이디의 세션 QR이 생성됩니다.
                    """
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "QR 생성 성공"),
                    @ApiResponse(responseCode = "500", description = "QR 생성 실패")
            }
    )
    @PostMapping("/{qrType}/{Id}")
    void createQR(
            @PathVariable String qrType,
            @PathVariable Long Id
    );

    @Operation(
            summary = "Session QR 체크 API",
            description = "사용자의 세션 입장을 체크합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "세션 체크 성공"),
                    @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음")
            }
    )
    @GetMapping("/session/{sessionId}")
    ResponseEntity<QrCheckDto> checkSessionQr(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(
            summary = "Event QR 체크 API",
            description = "사용자의 이벤트 입장을 체크합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "이벤트 체크 성공"),
                    @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음")
            }
    )
    @GetMapping("/event/{eventId}")
    ResponseEntity<QrCheckDto> checkEventQr(
            @PathVariable Long eventId,
            @AuthenticationPrincipal UserDetails userDetails
    );
}
