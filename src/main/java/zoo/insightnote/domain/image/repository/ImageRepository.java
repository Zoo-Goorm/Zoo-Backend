package zoo.insightnote.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}