package zoo.insightnote.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.entity.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByFileUrlIn(List<String> fileUrls);

    List<Image> findByEntityIdAndEntityType(Long entityId, EntityType entityType);

}