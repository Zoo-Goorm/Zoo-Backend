package zoo.insightnote.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zoo.insightnote.domain.reservation.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r.session.id FROM Reservation r WHERE r.user.username = :username")
    List<Long> findSessionIdsByUserId(@Param("username") String username);

    @Query("SELECT r FROM Reservation r WHERE r.user.username = :username and r.session.id = :sessionId")
    Optional<Reservation> findReservedSession(@Param("username") String username, @Param("sessionId") Long sessionId);
}
