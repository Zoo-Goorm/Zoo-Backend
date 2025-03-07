package zoo.insightnote.domain.session.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.session.dto.SessionRequest;
import zoo.insightnote.domain.session.dto.SessionResponse;

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
    ResponseEntity<SessionResponse.Default> createSession(
            @RequestBody SessionRequest.Create request
    );

    @Operation(summary = "세션 수정", description = "기존 세션 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "세션 수정 성공"),
            @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{sessionId}")
    ResponseEntity<SessionResponse.Default> updateSession(
            @Parameter(description = "수정할 세션 ID") @PathVariable Long sessionId,
            @RequestBody SessionRequest.Update request
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

    @Operation(summary = "특정 세션 조회", description = "ID를 이용해 특정 세션 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "세션 조회 성공"),
            @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{sessionId}")
    ResponseEntity<SessionResponse.Default> getSessionById(
            @Parameter(description = "조회할 세션 ID") @PathVariable Long sessionId
    );
}