package zoo.insightnote.domain.faq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.faq.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long> {
}
