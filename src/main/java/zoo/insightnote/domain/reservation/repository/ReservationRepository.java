package zoo.insightnote.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
