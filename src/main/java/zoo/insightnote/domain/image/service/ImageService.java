package zoo.insightnote.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.entity.Image;
import zoo.insightnote.domain.image.repository.ImageRepository;
import zoo.insightnote.domain.s3.service.S3Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

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

    @Transactional
    public void updateImages(ImageRequest.UploadImages request) {
        Long entityId = request.entityId();
        EntityType entityType = request.entityType();
        List<ImageRequest.UploadImage> newImages = request.images();

        // 기존 DB에 저장된 이미지 리스트 가져오기
        List<Image> existingImages = imageRepository.findByEntityIdAndEntityType(entityId, entityType);
        List<String> existingImageUrls = existingImages.stream()
                .map(Image::getFileUrl)
                .collect(Collectors.toList());

        // 새로운 이미지 URL 리스트 추출
        List<String> newImageUrls = newImages.stream()
                .map(ImageRequest.UploadImage::fileUrl)
                .collect(Collectors.toList());

        // 삭제할 이미지 찾기 (기존에 있었는데 요청에서는 사라진 이미지)
        List<String> deletedImageUrls = existingImageUrls.stream()
                .filter(url -> !newImageUrls.contains(url))
                .collect(Collectors.toList());

        // 추가할 이미지 찾기 (요청에서 새롭게 추가된 이미지)
        List<ImageRequest.UploadImage> addedImages = newImages.stream()
                .filter(img -> !existingImageUrls.contains(img.fileUrl()))
                .collect(Collectors.toList());

        // S3 & DB에서 삭제할 이미지가 있으면 삭제
        if (!deletedImageUrls.isEmpty()) {
            s3Service.deleteImages(deletedImageUrls);
            imageRepository.deleteByFileUrlIn(deletedImageUrls);
        }

        // 새롭게 추가된 이미지 저장
        if (!addedImages.isEmpty()) {
            List<Image> imagesToSave = addedImages.stream()
                    .map(img -> Image.of(img.fileName(), img.fileUrl(), entityId, entityType))
                    .collect(Collectors.toList());
            imageRepository.saveAll(imagesToSave);
        }
    }

    @Transactional
    public void deleteImagesByEntity(Long entityId, EntityType entityType) {
        List<Image> images = imageRepository.findByEntityIdAndEntityType(entityId, entityType);

        if (images.isEmpty()) return;

        List<String> imageUrls = images.stream()
                .map(Image::getFileUrl)
                .collect(Collectors.toList());

        s3Service.deleteImages(imageUrls);

        imageRepository.deleteByEntityIdAndEntityType(entityId, entityType);
    }

}