package zoo.insightnote.domain.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.repository.EventRepository;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.entity.Image;
import zoo.insightnote.domain.image.repository.ImageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public Event createEventWithImages(Event event, List<String> imageUrls) {
        // 행사 저장
        Event savedEvent = eventRepository.save(event);

        // 이미지 저장
        List<Image> images = imageUrls.stream()
                .map(url -> Image.of("event-image", url, savedEvent.getId(), EntityType.EVENT))
                .collect(Collectors.toList());

        imageRepository.saveAll(images);
        return savedEvent;
    }
}