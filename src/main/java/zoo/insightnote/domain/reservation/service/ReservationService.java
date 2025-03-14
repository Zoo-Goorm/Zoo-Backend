package zoo.insightnote.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.reservation.repository.ReservationQueryRepository;
import zoo.insightnote.domain.reservation.repository.ReservationRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final ReservationQueryRepository reservationQueryRepository;

    public Map<String, Object> getUserTicketInfo(Long userId) {
        Map<String, Object> userTicketInfo = reservationQueryRepository.findUserTicketInfo(userId);
        return userTicketInfo;
    }
}
