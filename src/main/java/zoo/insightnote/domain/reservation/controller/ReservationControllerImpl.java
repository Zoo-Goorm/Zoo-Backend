package zoo.insightnote.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.reservation.dto.response.UserTicketInfoResponseDto;
import zoo.insightnote.domain.reservation.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationControllerImpl implements ReservationController{
    private final ReservationService reservationService;

    @GetMapping("/ticket/{userId}")
    public ResponseEntity<UserTicketInfoResponseDto> getUserTicketInfo(@PathVariable Long userId) {
        UserTicketInfoResponseDto userTicketInfo = reservationService.getUserTicketInfo(userId);
        return ResponseEntity.ok(userTicketInfo);
    }

    // TODO: 유저아이디 토큰에서 가져오기 적용 후 수정
    @PostMapping("/{sessionId}/{userId}")
    public ResponseEntity<Void> addSession(@PathVariable Long sessionId, @PathVariable Long userId) {
        reservationService.addSession(sessionId, userId);
        return ResponseEntity.noContent().build();
    }
}
