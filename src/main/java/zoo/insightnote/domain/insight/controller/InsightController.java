package zoo.insightnote.domain.insight.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;

@Tag(name = "INSIGHT", description = "인사이트 관련 API")
@RequestMapping("/api/v1/insights")
public interface InsightController {

    @Operation(summary = "인사이트 생성", description = "새로운 인사이트를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인사이트 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    ResponseEntity<InsightResponseDto.InsightRes> createInsight(
            @RequestBody InsightRequestDto.CreateDto request
    );

    @Operation(summary = "인사이트 수정", description = "기존 인사이트 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인사이트 수정 성공"),
            @ApiResponse(responseCode = "404", description = "인사이트를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{insightId}")
    ResponseEntity<InsightResponseDto.InsightRes> updateInsight(
            @Parameter(description = "수정할 인사이트 ID") @PathVariable Long insightId,
            @RequestBody InsightRequestDto.UpdateDto request
    );

    @Operation(summary = "인사이트 삭제", description = "특정 ID의 인사이트를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "인사이트 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "인사이트를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{insightId}")
    ResponseEntity<Void> deleteInsight(
            @Parameter(description = "삭제할 인사이트 ID") @PathVariable Long insightId
    );

    @Operation(summary = "특정 인사이트 조회", description = "ID를 이용해 특정 인사이트 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인사이트 조회 성공"),
            @ApiResponse(responseCode = "404", description = "인사이트를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{insightId}")
    ResponseEntity<InsightResponseDto.InsightRes> getInsightById(
            @Parameter(description = "조회할 인사이트 ID") @PathVariable Long insightId
    );
}