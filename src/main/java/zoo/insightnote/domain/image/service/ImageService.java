package zoo.insightnote.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.entity.Image;
import zoo.insightnote.domain.image.repository.ImageRepository;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public Image saveImage(String fileName, String fileUrl, EntityType entityType, Long entityId) {
        Image image = Image.of(fileName, fileUrl, entityId, entityType);
        return imageRepository.save(image);
    }
}