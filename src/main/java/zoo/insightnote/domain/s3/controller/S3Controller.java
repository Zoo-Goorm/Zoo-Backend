package zoo.insightnote.domain.s3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.image.entity.EntityType;

import java.util.List;

@Tag(name = "S3", description = "S3 Presigned URL API")  // Swagger 태그 추가
@RequestMapping("/s3")
public interface S3Controller {

    @Operation(summary = "Presigned URL 요청", description = """
            - S3 Presigned URL을 생성하는 API입니다.
            - 이 API로 받은 URL그대로 요청을 보내면 되고 요청을 날릴때 Body의 binary에 이미지 파일을 첨부하면 S3에 이미지를 직접 업로드할 수 있습니다.
        """)
    @ApiResponse(
            responseCode = "200",
            description = "Presigned URL 리스트 반환 성공",
            content = @Content(schema = @Schema(implementation = String.class))
    )
    @PostMapping("/presigned-urls")
    ResponseEntity<List<String>> getPresignedUrls(
            @RequestParam EntityType entityType,
            @RequestBody List<String> fileNames
    );
}