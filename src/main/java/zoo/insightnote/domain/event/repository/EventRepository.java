package zoo.insightnote.domain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
