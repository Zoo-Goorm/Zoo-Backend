package zoo.insightnote.domain.s3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.s3.service.S3Service;

import java.util.List;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/presigned-urls")
    public ResponseEntity<List<String>> getPresignedUrls(@RequestParam EntityType entityType, @RequestBody List<String> fileNames) {
        List<String> urls = s3Service.getPresignedUrlsForImages(entityType, fileNames);
        return ResponseEntity.ok(urls);
    }
}