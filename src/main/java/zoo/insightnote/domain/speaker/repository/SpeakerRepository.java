package zoo.insightnote.domain.speaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.speaker.entity.Speaker;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
}
