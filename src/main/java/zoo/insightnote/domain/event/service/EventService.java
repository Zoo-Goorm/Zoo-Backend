package zoo.insightnote.domain.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.event.dto.req.EventUpdateRequestDto;
import zoo.insightnote.domain.event.dto.res.EventResponseDto;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.repository.EventRepository;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.entity.Image;
import zoo.insightnote.domain.image.repository.ImageRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public Event createEventWithImages(Event event, List<String> imageUrls) {

        Event savedEvent = eventRepository.save(event);

        // 이미지 저장(클라이언트에서 전달)
        List<Image> images = imageUrls.stream()
                .map(url -> Image.of("event-image", url, savedEvent.getId(), EntityType.EVENT))
                .collect(Collectors.toList());

        imageRepository.saveAll(images);
        return savedEvent;
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

        eventRepository.delete(event);
    }
}