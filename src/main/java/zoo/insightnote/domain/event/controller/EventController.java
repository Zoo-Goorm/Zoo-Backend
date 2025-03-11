package zoo.insightnote.domain.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.event.dto.req.EventRequestDto;
import zoo.insightnote.domain.event.dto.req.EventUpdateRequestDto;
import zoo.insightnote.domain.event.dto.res.EventResponseDto;

@Tag(name = "EVENT", description = "이벤트 관련 API")  // Swagger 태그
@RequestMapping("/api/v1/events")
public interface EventController {

    @Operation(summary = "이벤트 생성 API", description = "새로운 이벤트를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "이벤트 생성 성공", content = @Content(schema = @Schema(implementation = EventResponseDto.class)))
    @PostMapping
    ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto request);

    @Operation(summary = "이벤트 조회 API", description = "특정 ID의 이벤트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "이벤트 조회 성공", content = @Content(schema = @Schema(implementation = EventResponseDto.class)))
    @GetMapping("/{id}")
    ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id);

    @Operation(summary = "이벤트 수정 API", description = "이벤트 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "이벤트 수정 성공", content = @Content(schema = @Schema(implementation = EventResponseDto.class)))
    @PutMapping("/{id}")
    ResponseEntity<EventResponseDto> updateEvent(@PathVariable Long id, @RequestBody EventUpdateRequestDto request);

    @Operation(summary = "이벤트 삭제 API", description = "이벤트를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "이벤트 삭제 성공")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEvent(@PathVariable Long id);
}