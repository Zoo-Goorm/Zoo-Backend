package zoo.insightnote.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.reservation.service.ReservationService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationControllerImpl implements ReservationController{
    private final ReservationService reservationService;

    @GetMapping("/ticket/{userId}")
    public Map<String, Object> getUserTicketInfo(@PathVariable Long userId) {
        Map<String, Object> userTicketInfo = reservationService.getUserTicketInfo(userId);
        return userTicketInfo;
    }
}
