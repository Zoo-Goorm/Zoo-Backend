package zoo.insightnote.domain.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.event.dto.req.EventUpdateRequestDto;
import zoo.insightnote.domain.event.dto.req.EventRequestDto;
import zoo.insightnote.domain.event.dto.res.EventResponseDto;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.repository.EventRepository;
import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.repository.ImageRepository;
import zoo.insightnote.domain.image.service.ImageService;
import zoo.insightnote.domain.s3.service.S3Service;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;
    private final ImageService imageService;

    @Transactional
    public EventResponseDto createEvent(EventRequestDto request) {
        Event event = Event.builder()
                .name(request.name())
                .description(request.description())
                .location(request.location())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .build();

        Event savedEvent = eventRepository.save(event);

        // 이미지 저장 (클라이언트에서 전달)
        imageService.saveImages(savedEvent.getId(), EntityType.EVENT, request.images());

        return EventResponseDto.fromEntity(savedEvent);
    }

    // 세션 생성시 이벤트 객체를 받기 위한 메서드
    public Event findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(ErrorCode.EVENT_NOT_FOUND));
    }

    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EVENT_NOT_FOUND));
        return EventResponseDto.fromEntity(event);
    }

    @Transactional
    public EventResponseDto updateEvent(Long id, EventUpdateRequestDto request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EVENT_NOT_FOUND));

        imageService.updateImages(new ImageRequest.UploadImages(
                event.getId(),
                EntityType.EVENT,
                request.images()
        ));

        event.update(
                request.name(),
                request.description(),
                request.location(),
                request.startTime(),
                request.endTime()
        );

        return EventResponseDto.fromEntity(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EVENT_NOT_FOUND));

        imageService.deleteImagesByEntity(event.getId(), EntityType.EVENT);

        eventRepository.delete(event);
    }
}