package zoo.insightnote.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.reservation.dto.response.UserTicketInfoResponseDto;
import zoo.insightnote.domain.reservation.repository.ReservationCustomQueryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final ReservationCustomQueryRepository reservationQueryRepository;

    public UserTicketInfoResponseDto getUserTicketInfo(Long userId) {
        UserTicketInfoResponseDto userTicketInfo = reservationQueryRepository.processUserTicketInfo(userId);
        return userTicketInfo;
    }
}
