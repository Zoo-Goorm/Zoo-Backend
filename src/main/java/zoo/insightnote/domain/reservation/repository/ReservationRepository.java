package zoo.insightnote.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zoo.insightnote.domain.reservation.entity.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r.session.id FROM Reservation r WHERE r.user.id = :userId")
    List<Long> findSessionIdsByUserId(@Param("userId") Long userId);
}
