package zoo.insightnote.domain.session.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.session.dto.SessionRequestDto;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.dto.request.SessionCreateRequest;
import zoo.insightnote.domain.session.dto.response.SessionCreateResponse;

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
    ResponseEntity<SessionCreateResponse> createSession(
            @RequestBody SessionCreateRequest request
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

}