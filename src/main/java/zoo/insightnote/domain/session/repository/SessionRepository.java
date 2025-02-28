package zoo.insightnote.domain.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.session.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
