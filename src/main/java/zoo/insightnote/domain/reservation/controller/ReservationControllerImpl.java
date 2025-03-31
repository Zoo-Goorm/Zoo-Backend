package zoo.insightnote.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.reservation.dto.response.UserTicketInfoResponse;
import zoo.insightnote.domain.reservation.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationControllerImpl implements ReservationController{
    private final ReservationService reservationService;

    @GetMapping("/ticket")
    public ResponseEntity<UserTicketInfoResponse> getUserTicketInfo(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserTicketInfoResponse userTicketInfo = reservationService.getUserTicketInfo(userDetails.getUsername());
        return ResponseEntity.ok(userTicketInfo);
    }

    @PostMapping("/{sessionId}")
    public ResponseEntity<Void> addSession(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        reservationService.addSession(sessionId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> cancelSession(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        reservationService.cancelSession(sessionId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cancelSessionId}/{addSessionId}")
    public ResponseEntity<Void> cancelAndAddSession(
            @PathVariable Long cancelSessionId,
            @PathVariable Long addSessionId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        reservationService.cancelAndAddSession(cancelSessionId, addSessionId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
