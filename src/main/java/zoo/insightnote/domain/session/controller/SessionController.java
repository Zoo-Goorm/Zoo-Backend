package zoo.insightnote.domain.session.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.session.dto.SessionRequestDto;
import zoo.insightnote.domain.session.dto.SessionResponseDto;

import java.util.List;
import java.util.Map;

@Tag(name = "SESSION", description = "세션 관련 API")
@RequestMapping("/api/v1/sessions")
public interface SessionController {

    @Operation(summary = "세션 생성", description = "새로운 세션을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "세션 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    ResponseEntity<SessionResponseDto.SessionRes> createSession(
            @RequestBody SessionRequestDto.Create request
    );

    @Operation(summary = "세션 수정", description = "기존 세션 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "세션 수정 성공"),
            @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{sessionId}")
    ResponseEntity<SessionResponseDto.SessionRes> updateSession(
            @Parameter(description = "수정할 세션 ID") @PathVariable Long sessionId,
            @RequestBody SessionRequestDto.Update request
    );

    @Operation(summary = "세션 삭제", description = "특정 ID의 세션을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "세션 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{sessionId}")
    ResponseEntity<Void> deleteSession(
            @Parameter(description = "삭제할 세션 ID") @PathVariable Long sessionId
    );

    @Operation(
            summary = "세션 전체 조회 (이미지 제외, 인원수 제외)",
            description = "모든 세션 정보를 조회하되, 연사 이미지와 인원수는 제외하고 키워드만 포함합니다. "
                    + "응답은 날짜별로 세션을 그룹화하여 제공합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "세션 전체 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    ResponseEntity<Map<String, List<SessionResponseDto.SessionAllRes>>> getAllSessions();

    @Operation(summary = "세션 상세 조회", description = "연사 이미지, 인원수, 키워드 포함 세션 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "세션 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/detailed")
    ResponseEntity<Map<String, List<SessionResponseDto.SessionDetailedRes>>> getAllSessionsWithDetails();

    @Operation(summary = "세션 단일 상세 조회", description = "특정 세션 ID로 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "세션 조회 성공"),
            @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{sessionId}")
    ResponseEntity<SessionResponseDto.SessionSpeakerDetailRes> getSessionDetails(
            @Parameter(description = "조회할 세션 ID") @PathVariable Long sessionId
    );

    @Operation(
            summary = "특정 세션의 인사이트 목록 조회",
            description = """
        특정 세션 ID에 해당하는 인사이트 목록을 조회합니다.

        - 로그인한 사용자가 해당 세션에 작성한 '임시 저장글(draft)'이 있다면 가장 먼저 반환됩니다.
        - 그 다음에는 일반 공개 인사이트가 정렬 조건에 따라 나열됩니다.
          - 정렬 방식: `latest` (최신순, 기본값), `likes` (좋아요순)
        - 페이지네이션 적용: page (0부터 시작), size 지정 가능

        요청 예시:  
        `/api/v1/sessions/2/insight-notes?sort=likes&page=0&size=10`
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인사이트 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
    })
    @GetMapping("/{sessionId}/insight-notes")
    ResponseEntity<InsightResponseDto.SessionInsightListPageRes> getInsightsBySession(
            @Parameter(description = "세션 ID") @PathVariable Long sessionId,
            @Parameter(description = "정렬 조건 (latest | likes)", example = "latest") @RequestParam(defaultValue = "latest") String sort,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "5") @RequestParam(defaultValue = "5") int size
    );
}