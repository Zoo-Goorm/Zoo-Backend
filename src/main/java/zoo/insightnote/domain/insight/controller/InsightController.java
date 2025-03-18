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

    @Operation(summary = "인사이트 저장,생성",
            description = """
           사용자가 저장 또는 임시 저장을 요청할 경우, 해당 인사이트를 저장하거나 업데이트합니다.
           - **정식 저장**: `isDraft = false`로 설정하면 인사이트가 정식 저장됩니다.
           - **임시 저장**: `isDraft = true`로 설정하면 사용자가 작성 중인 상태로 저장됩니다.
           
           이 API는 기존의 인사이트가 존재하면 업데이트하고, 없으면 새로운 인사이트를 생성합니다.
           - `sessionId`: 인사이트가 속한 세션 ID
           - `userId`: 인사이트를 작성한 사용자 ID
           - `memo`: 인사이트 내용
           - `isPublic`: 공개 여부 (`true`면 공개, `false`면 비공개)
           - `isAnonymous`: 익명 여부 (`true`면 익명으로 표시, `false`면 실명 표시)
           - `isDraft`: 임시 저장 여부 (`true`면 임시 저장, `false`면 정식 저장)
           - `images`: 첨부할 이미지 리스트
           """)
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

    @Operation(summary = "좋아요 등록/취소",
            description = """
           특정 인사이트에 대해 사용자가 좋아요를 등록하거나 취소합니다.
           - **좋아요 등록**: 사용자가 해당 인사이트에 좋아요를 누릅니다.
           - **좋아요 취소**: 사용자가 기존에 눌렀던 좋아요를 취소합니다.
           
           유저는 **자신이 작성한 인사이트에는 좋아요를 누를 수 없습니다.**
           
           요청 파라미터:
           - `insightId`: 좋아요를 등록할 인사이트의 ID
           - `userId`: 좋아요를 누르는 사용자 ID
           
           반환 값:
           - 좋아요가 등록되었을 경우 `"좋아요가 등록되었습니다."`
           - 좋아요가 취소되었을 경우 `"좋아요가 취소되었습니다."`
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 등록 또는 취소 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "자신의 인사이트에 좋아요를 누를 수 없음"),
            @ApiResponse(responseCode = "404", description = "인사이트 또는 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{insightId}/like")
    ResponseEntity<String> toggleLike(
            @Parameter(description = "좋아요를 등록/취소할 인사이트 ID") @PathVariable Long insightId,
            @Parameter(description = "좋아요를 누르는 사용자 ID") @RequestParam Long userId
    );
}