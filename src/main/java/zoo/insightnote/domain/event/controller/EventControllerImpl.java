package zoo.insightnote.domain.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.event.dto.req.EventRequestDto;
import zoo.insightnote.domain.event.dto.req.EventUpdateRequestDto;
import zoo.insightnote.domain.event.dto.res.EventResponseDto;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.service.EventService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventControllerImpl implements EventController {
    private final EventService eventService;

    @Override
    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto request) {
        EventResponseDto savedEvent = eventService.createEvent(request);
        return ResponseEntity.ok(savedEvent);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        EventResponseDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable Long id,
            @RequestBody EventUpdateRequestDto request) {
        EventResponseDto updatedEvent = eventService.updateEvent(id, request);
        return ResponseEntity.ok(updatedEvent);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}