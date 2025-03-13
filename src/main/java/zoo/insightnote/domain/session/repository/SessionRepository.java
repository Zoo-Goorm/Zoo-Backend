package zoo.insightnote.domain.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.entity.Session;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

}


