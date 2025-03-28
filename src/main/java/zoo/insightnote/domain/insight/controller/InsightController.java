package zoo.insightnote.domain.insight.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.dto.request.InsightCreateRequest;
import zoo.insightnote.domain.insight.dto.request.InsightUpdateRequest;
import zoo.insightnote.domain.insight.dto.response.InsightIdResponse;
import zoo.insightnote.domain.insight.dto.response.InsightListResponse;
import zoo.insightnote.domain.insight.dto.response.InsightTopListResponse;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "INSIGHT", description = "인사이트 관련 API")
@RequestMapping("/api/v1/insights")
public interface InsightController {

//    @Operation(summary = "인사이트 저장,생성",
//            description = """
//           사용자가 저장 또는 임시 저장을 요청할 경우, 해당 인사이트를 저장하거나 업데이트합니다.
//           - **정식 저장**: `isDraft = false`로 설정하면 인사이트가 정식 저장됩니다.
//           - **임시 저장**: `isDraft = true`로 설정하면 사용자가 작성 중인 상태로 저장됩니다.
//
//           이 API는 기존의 인사이트가 존재하면 업데이트하고, 없으면 새로운 인사이트를 생성합니다.
//           - `sessionId`: 인사이트가 속한 세션 ID
//           - `userId`: 인사이트를 작성한 사용자 ID
//           - `memo`: 인사이트 내용
//           - `isPublic`: 공개 여부 (`true`면 공개, `false`면 비공개)
//           - `isAnonymous`: 익명 여부 (`true`면 익명으로 표시, `false`면 실명 표시)
//           - `isDraft`: 임시 저장 여부 (`true`면 임시 저장, `false`면 정식 저장)
//           - `images`: 첨부할 이미지 리스트
//           """)
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "인사이트 생성 성공"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//    @PostMapping
//    ResponseEntity<InsightResponseDto.InsightRes> createInsight(
//            @RequestBody InsightRequestDto.CreateDto request
//    );



    @Operation(summary = "인사이트 생성 (메모만 저장)",
            description = """
        인사이트를 새로 생성합니다.  
        - 이 API는 메모만 저장되며, 투표나 이미지 정보는 포함되지 않습니다.  
        - 성공 시 생성된 인사이트의 ID만 반환됩니다.
        
        - `isPublic`: 공개 여부 (`true`면 공개, `false`면 비공개)
        - `isAnonymous`: 익명 여부 (`true`면 익명으로 표시, `false`면 실명 표시)
        - `isDraft`: 임시 저장 여부 (`true`면 임시 저장, `false`면 정식 저장)
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인사이트 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    })
    @PostMapping("/insights")
    ResponseEntity<InsightIdResponse> createInsight(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody InsightCreateRequest request
    );

    @Operation(summary = "인사이트 수정",
            description = """
                인사이트 ID를 기반으로 메모, 공개 여부, 익명 여부 등의 내용을 수정합니다.
                - 변경된 필드만 업데이트되며, 변경이 없으면 기존 값 유지됩니다.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인사이트 수정 성공"),
            @ApiResponse(responseCode = "404", description = "인사이트를 찾을 수 없음"),
    })
    @PutMapping("/{insightId}")
    ResponseEntity<InsightIdResponse> updateInsight(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "수정할 인사이트 ID", example = "1")
            @PathVariable Long insightId,
            @RequestBody InsightUpdateRequest request
    );


//    @Operation(summary = "인사이트 수정", description = "기존 인사이트 정보를 수정합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "인사이트 수정 성공"),
//            @ApiResponse(responseCode = "404", description = "인사이트를 찾을 수 없음"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//    @PutMapping("/{insightId}")
//    ResponseEntity<InsightResponseDto.InsightRes> updateInsight(
//            @Parameter(description = "수정할 인사이트 ID") @PathVariable Long insightId,
//            @RequestBody InsightRequestDto.UpdateDto request
//    );

    @Operation(
            summary = "인사이트 삭제",
            description = """
                특정 ID의 인사이트를 삭제합니다.
                - 로그인한 사용자 본인이 작성한 인사이트만 삭제할 수 있습니다.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "인사이트 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "인사이트를 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "해당 인사이트에 대한 삭제 권한이 없음"),
    })
    @DeleteMapping("/{insightId}")
    ResponseEntity<Void> deleteInsight(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "삭제할 인사이트 ID", example = "1")
            @PathVariable Long insightId
    );

//    @Operation(summary = "특정 인사이트 조회", description = "ID를 이용해 특정 인사이트 정보를 조회합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "인사이트 조회 성공"),
//            @ApiResponse(responseCode = "404", description = "인사이트를 찾을 수 없음"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//    @GetMapping("/{insightId}")
//    ResponseEntity<InsightResponseDto.InsightRes> getInsightById(
//            @Parameter(description = "조회할 인사이트 ID") @PathVariable Long insightId
//    );

    @Operation(summary = "인기 인사이트 상위 3개 조회",
            description = """
            좋아요 수를 기준으로 가장 인기 있는 인사이트 3개를 조회합니다.
            - 익명 여부가 true 경우 nickname , false 인경우 name을 반환합니다. 
            - 좋아요 수가 많은 순서대로 정렬되어 반환됩니다.  
            - 각 인사이트에는 최신 이미지와 댓글 수가 포함됩니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상위 인기 인사이트 3개 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/top")
    ResponseEntity<List<InsightTopListResponse>> getTop3PopularInsights(
            @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(
            summary = "인사이트 목록 조회",
            description = """
        세션 날짜(eventDay)에 해당하는 인사이트 목록을 조회합니다.
        - 사용자가 비공개로 설정한 인사이트 경우에는 해당 리스트 목록에서 제외됩니다.
        - 익명여부가 true인경우 nickname을 반환하고 false 인경우 name을 반환합니다 
    
        - 정렬: `latest` (최신순, 기본값), `likes` (좋아요순)
        - 세션 필터링: 특정 세션 ID로 필터링 가능
        - 페이징: page 번호 (0부터 시작)
    
        요청 예시:
        `/api/v1/insights/list?eventDay=2025-04-04&sort=likes&sessionId=2&page=0`
    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인사이트 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 인사이트를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Not Found",
                                    value = "{ \"message\": \"해당 인사이트를 찾을 수 없습니다.\" }"
                            )
                    )
            ),
    })
    @GetMapping("/list")
    ResponseEntity<InsightListResponse> getInsights(
            @Parameter(description = "세션 날짜 (선택)", required = false)
            @RequestParam(value = "eventDay", required = false ) LocalDate eventDay,

            @Parameter(description = "선택적 세션 ID")
            @RequestParam(value = "sessionId", required = false) Long sessionId,

            @Parameter(description = "정렬 기준: latest(최신순), likes(좋아요순)", example = "latest")
            @RequestParam(value = "sort", defaultValue = "latest") String sort,

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(summary = "인사이트 상세 조회",
            description = """
            특정 인사이트 ID에 해당하는 인사이트 상세 정보를 조회합니다.  
            - 작성자 정보, 좋아요 수, 투표 정보, 세션 정보 등이 포함됩니다.  
            - 비공개 인사이트이거나 존재하지 않는 경우에는 예외가 발생합니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인사이트 상세 조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 인사이트를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Not Found",
                                    value = "{ \"message\": \"해당 인사이트를 찾을 수 없습니다.\" }"
                            )
                    )
            )
    })
    @GetMapping("/{insightId}")
    ResponseEntity<InsightResponseDto.InsightDetailPageRes> getInsightDetail(
            @Parameter(description = "상세 정보를 조회할 인사이트 ID", example = "1") @PathVariable Long insightId,
            @AuthenticationPrincipal UserDetails userDetails
    );


    @Operation(summary = "좋아요 등록/취소",
            description = """
           특정 인사이트에 대해 사용자가 좋아요를 등록하거나 취소합니다.
           - **좋아요 등록**: 사용자가 해당 인사이트에 좋아요를 누릅니다.
           - **좋아요 취소**: 사용자가 기존에 눌렀던 좋아요를 취소합니다.
           
           유저는 **자신이 작성한 인사이트에는 좋아요를 누를 수 없습니다.**
           
           요청 파라미터:
           - `insightId`: 좋아요를 등록할 인사이트의 ID
           
           반환 값:
           - 좋아요가 등록되었을 경우 `"좋아요가 등록되었습니다."`
           - 좋아요가 취소되었을 경우 `"좋아요가 취소되었습니다."`
           """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 등록 또는 취소 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "자신의 인사이트에 좋아요를 누를 수 없음"),
    })
    @PostMapping("/{insightId}/like")
    ResponseEntity<String> toggleLike(
            @Parameter(description = "좋아요를 등록/취소할 인사이트 ID") @PathVariable Long insightId,
            @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(
            summary = "특정 세션의 인사이트 목록 조회",
            description = """
        특정 세션 ID에 해당하는 인사이트 목록을 조회합니다.

        - 익명여부가 true인 경우 nickname을 출력하고 false 인경우 name을 반환합니다.
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
            @Parameter(description = "페이지 크기", example = "5") @RequestParam(defaultValue = "5") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(
            summary = "내가 작성한 인사이트 목록 조회",
            description = """
        로그인한 사용자가 작성한 인사이트 목록을 조회합니다.

        - 임시 저장 여부, 공개 여부에 상관없이 **본인이 작성한 모든 인사이트**가 조회됩니다.
        - 정렬은 항상 최신순으로 고정됩니다.
        - `eventDay` 또는 `sessionId`를 통해 필터링할 수 있습니다.
        - 무한스크롤 또는 페이지네이션에 사용하기 위해 `page`, `size` 파라미터를 지원합니다.
    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "나의 인사이트 목록 조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 인사이트를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Not Found",
                                    value = """
                                        {
                                          "message": "해당 인사이트를 찾을 수 없습니다.",
                                        }
                                        """
                            )
                    )
            )
    })
    @GetMapping("/my/insights")
    ResponseEntity<InsightResponseDto.MyInsightListPageRes> getMyInsights(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,

            @Parameter(description = "세션 날짜 (예: 2025-04-04)", example = "2025-04-04")
            @RequestParam(required = false) LocalDate eventDay,

            @Parameter(description = "세션 ID (선택)")
            @RequestParam(required = false) Long sessionId,

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "5")
            @RequestParam(defaultValue = "5") int size
    );

}