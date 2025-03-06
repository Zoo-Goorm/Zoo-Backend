package zoo.insightnote.domain.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.event.dto.req.EventUpdateRequestDto;
import zoo.insightnote.domain.event.dto.req.EventRequestDto;
import zoo.insightnote.domain.event.dto.res.EventResponseDto;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.repository.EventRepository;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.entity.Image;
import zoo.insightnote.domain.image.repository.ImageRepository;
import zoo.insightnote.domain.s3.service.S3Service;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

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
        List<Image> images = request.imageUrls().stream()
                .map(url -> Image.of("event-image", url, savedEvent.getId(), EntityType.EVENT))
                .collect(Collectors.toList());

        imageRepository.saveAll(images);

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

        // 기존 DB에 저장된 이미지 리스트 가져오기
        List<String> existingImageUrls = imageRepository.findByEntityIdAndEntityType(event.getId(), EntityType.EVENT)
                .stream()
                .map(Image::getFileUrl)
                .collect(Collectors.toList());

        // 클라이언트에서 보낸 새로운 이미지 리스트
        List<String> newImageUrls = request.imageUrls();

        // 삭제할 이미지 찾기 (기존에 있었는데 요청에서는 사라진 이미지)
        List<String> deletedImageUrls = existingImageUrls.stream()
                .filter(url -> !newImageUrls.contains(url))
                .collect(Collectors.toList());

        // 추가할 이미지 찾기 (요청에서 새롭게 추가된 이미지)
        List<String> addedImageUrls = newImageUrls.stream()
                .filter(url -> !existingImageUrls.contains(url))
                .collect(Collectors.toList());

        // S3 & DB에서 삭제할 이미지가 있으면 삭제
        if (!deletedImageUrls.isEmpty()) {
            s3Service.deleteImages(deletedImageUrls);
            imageRepository.deleteByFileUrlIn(deletedImageUrls);
        }

        // 새롭게 추가된 이미지 저장
        if (!addedImageUrls.isEmpty()) {
            List<Image> addedImages = addedImageUrls.stream()
                    .map(url -> Image.of("event-image", url, event.getId(), EntityType.EVENT))
                    .collect(Collectors.toList());
            imageRepository.saveAll(addedImages);
        }

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

        List<String> imageUrls = imageRepository.findByEntityIdAndEntityType(event.getId(), EntityType.EVENT)
                .stream()
                .map(Image::getFileUrl)
                .collect(Collectors.toList());

        if (!imageUrls.isEmpty()) {
            s3Service.deleteImages(imageUrls);
        }
        imageRepository.deleteByEntityIdAndEntityType(event.getId(), EntityType.EVENT);

        eventRepository.delete(event);
    }
}