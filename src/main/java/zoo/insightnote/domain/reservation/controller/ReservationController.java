package zoo.insightnote.domain.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.reservation.dto.response.UserTicketInfoResponseDto;

@Tag(name = "Reservation", description = "세션 예약 관련 API")
@RequestMapping("/api/v1/reservation")
public interface ReservationController {
    @Operation(
            summary = "결제 티켓 및 예약 세션 조회",
            description = "사용자가 구매한 티켓에 대한 정보와 예약 세션들을 조회할 수 있습니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "티켓, 세션 조회 성공"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/ticket/{userId}")
    ResponseEntity<UserTicketInfoResponseDto> getUserTicketInfo(Long userId);

    @Operation(
            summary = "세션 추가",
            description = "사용자가 세션을 추가할 수 있습니다." +
                    "기존 예약 세션과 동일한 시간대의 세션은 추가할 수 없습니다"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "세션 추가 성공"),
                    @ApiResponse(responseCode = "400", description = "세션 시간대 중복"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            }
    )
    @PostMapping("/{sessionId}/{userId}")
    ResponseEntity<Void> addSession(@PathVariable Long sessionId, @PathVariable Long userId);

    @Operation(
            summary = "세션 취소",
            description = "사용자가 세션을 취소할 수 있습니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "세션 취소 성공"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            }
    )
    @DeleteMapping("/{sessionId}/{userId}")
    ResponseEntity<Void> cancelSession(@PathVariable Long sessionId, @PathVariable Long userId);

    @Operation(
            summary = "세션 취소 후 신청",
            description = "사용자가 동일 시간대 세션이 있는 상태에서 세션을 신청하는 경우 기존의 세션 취소 후, 새 세션 신청을 할 수 있습니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "세션 취소 후 신청 성공"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            }
    )
    @PostMapping("/{cancelSessionId}/{addSessionId}/{userId}")
    ResponseEntity<Void> cancelAndAddSession(@PathVariable Long cancelSessionId, @PathVariable Long addSessionId, @PathVariable Long userId);
}
