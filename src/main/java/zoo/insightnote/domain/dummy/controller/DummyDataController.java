package zoo.insightnote.domain.dummy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Dummy Data", description = "Dummy Data 생성 관련 API")
@RequestMapping("/dummy")
public interface DummyDataController {
    @Operation(
            summary = "더미 유저 생성",
            description = "입력한 수만큼 랜덤 유저 데이터를 생성합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User Dummy Data 생성 성공")
            }
    )
    @PostMapping("/users")
    ResponseEntity<String> generateDummyUsers(
            @Parameter(description = "생성할 유저 수", example = "100") @RequestParam int count);

    @Operation(
            summary = "더미 인사이트 생성",
            description = "입력한 수만큼 랜덤 인사이트 데이터를 생성합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Insight Dummy Data 생성 성공")
            }
    )
    @PostMapping("/insights")
    ResponseEntity<String> generateDummyInsights(
            @Parameter(description = "생성할 인사이트 수", example = "200") @RequestParam int count);

    @Operation(
            summary = "더미 결제 내역 생성",
            description = "입력한 수만큼 랜덤 결제 내역을 생성합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Payment, Reservation Dummy Data 생성 성공")
            }
    )
    @PostMapping("/payment")
    ResponseEntity<String> generateDummyPayments(
            @Parameter(description = "생성할 결제 내역 수", example = "200") @RequestParam int count);
}
