package zoo.insightnote.domain.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestReadyDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponseDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayReadyResponseDto;

@Tag(name = "PAYMENT", description = "결제 관련 API")
@RequestMapping("/api/v1/payment")
public interface PaymentController {
    @Operation(
            summary = "결제 요청 API",
            description = "카카오페이 API에 결제를 요청합니다."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "결제 요청 성공")
            }
    )
    @PostMapping("/request")
    ResponseEntity<KakaoPayReadyResponseDto> requestPayment(@RequestBody PaymentRequestReadyDto request);

    @Operation(
            summary = "결제 승인 API",
            description = "카카오페이 API 측에서 결제를 승인합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "결제 승인 성공")
            }
    )
    @GetMapping("/approve")
    ResponseEntity<KakaoPayApproveResponseDto> approvePayment(@RequestParam Long orderId,
                                                              @RequestParam Long userId,
                                                              @RequestParam String pgToken,
                                                              @AuthenticationPrincipal UserDetails userDetails);
}