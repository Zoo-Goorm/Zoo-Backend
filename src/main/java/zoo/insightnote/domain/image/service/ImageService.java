package zoo.insightnote.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.entity.Image;
import zoo.insightnote.domain.image.repository.ImageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void saveImages(Long entityId, EntityType entityType, List<ImageRequest.UploadImage> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        List<Image> imageEntities = images.stream()
                .map(image -> Image.of(image.fileName(), image.fileUrl(), entityId, entityType))
                .collect(Collectors.toList());

        imageRepository.saveAll(imageEntities);
    }

}