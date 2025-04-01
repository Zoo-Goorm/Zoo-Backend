package zoo.insightnote.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zoo.insightnote.domain.payment.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.user.username = :username and p.event.id = :eventId")
    Optional<List<Payment>> findReservedEvent(@Param("username") String username, @Param("eventId") Long eventId);
}
