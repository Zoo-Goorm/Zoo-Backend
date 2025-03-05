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
public class EventControllerImpl {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventRequestDto request) {
        Event event = Event.builder()
                .name(request.name())
                .description(request.description())
                .location(request.location())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .build();

        Event savedEvent = eventService.createEventWithImages(event, request.imageUrls());
        return ResponseEntity.ok(savedEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        EventResponseDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable Long id,
            @RequestBody EventUpdateRequestDto request) {
        EventResponseDto updatedEvent = eventService.updateEvent(id, request);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}