package zoo.insightnote.domain.s3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.image.entity.EntityType;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> getPresignedUrlsForImages(EntityType entityType, List<String> fileNames) {
        return fileNames.stream()
                .map(fileName -> createPresignedUrl(generateS3Key(entityType, fileName)).toExternalForm())
                .collect(Collectors.toList());
    }

    private String generateS3Key(EntityType entityType, String fileName) {
        return String.format("%s/%s-%s", entityType.name().toLowerCase(), UUID.randomUUID(), fileName);
    }

    private URL createPresignedUrl(String key) {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 10); // 10분 유효

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, key)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    public void deleteImages(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            String key = extractKeyFromUrl(imageUrl);
            amazonS3Client.deleteObject(bucket, key);
        }
    }

    private String extractKeyFromUrl(String imageUrl) {
        return imageUrl.replace("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/", "");
    }
}