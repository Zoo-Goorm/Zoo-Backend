package zoo.insightnote.domain.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.career.entity.Career;

public interface CareerRepository extends JpaRepository<Career, Long> {
}