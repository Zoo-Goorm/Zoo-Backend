package zoo.insightnote.domain.session.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.service.EventService;
import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.service.ImageService;
import zoo.insightnote.domain.session.dto.SessionRequest;
import zoo.insightnote.domain.session.dto.SessionResponse;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.entity.SessionStatus;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.speaker.entity.Speaker;
import zoo.insightnote.domain.speaker.service.SpeakerService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private EventService eventService;
    @Mock private SpeakerService speakerService;
    @Mock private ImageService imageService;

    @InjectMocks
    private SessionService sessionService;

    private Event event;
    private Speaker speaker;
    private Session session;

    @BeforeEach
    void setUp() {
        // 테스트용 Event, Speaker, Session 객체 초기화
        event = new Event(1L, "Tech Conference", "Event Description", "Location",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        speaker = new Speaker(1L, "요한", "yohan@example.com", "010-1234-5678");

        session = Session.builder()
                .id(1L)
                .event(event)
                .speaker(speaker)
                .eventDay(1)
                .name("AI Workshop")
                .shortDescription("짧은 설명")
                .longDescription("긴 설명")
                .maxCapacity(100)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .status(SessionStatus.BEFORE_START) //
                .videoLink("https://example.com")
                .location("2층")
                .build();
    }

    @Test
    @DisplayName("[세션 생성] 세션을 생성하면 요청한 정보가 저장되고, 관련 이미지도 함께 저장되어야 한다." +
            " / 현재 구현은 안되어 있지만 이미지와 S에 대한 테스트도 추가를 해야 할 것 같다")
    void createSession_Success() {
        //  Given (Mock 설정)
        SessionRequest.Create request = new SessionRequest.Create(
                event.getId(),
                speaker.getId(),
                session.getEventDay(),
                session.getName(),
                session.getShortDescription(),
                session.getLongDescription(),
                session.getMaxCapacity(),
                session.getStartTime(),
                session.getEndTime(),
                session.getStatus(),
                session.getVideoLink(),
                session.getLocation(),
                Collections.emptyList()
        );

        when(eventService.findById(event.getId())).thenReturn(event);
        when(speakerService.findById(speaker.getId())).thenReturn(speaker);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        //  When
        SessionResponse.Default response = sessionService.createSession(request);

        //  Then
        assertNotNull(response);
        assertEquals(session.getName(), response.name());
        verify(sessionRepository, times(1)).save(any(Session.class));
        verify(imageService, times(1)).saveImages(anyLong(), any(EntityType.class), anyList());
    }

    @Test
    @DisplayName("[세션 수정] 세션을 수정하면 새로운 정보가 반영되고, 변경된 이미지도 업데이트되어야 한다. " +
            "/ 현재 구현은 안되어 있지만 이미지와 S에 대한 테스트도 추가를 해야 할 것 같다")
    void updateSession_Success() {
        //  Given
        SessionRequest.Update request = new SessionRequest.Update(
                "세션1-2",
                "짧은 설명 수정",
                "긴 설명 수정",
                150,
                session.getStartTime(),
                session.getEndTime(),
                SessionStatus.COMPLETED,
                "https://updated-link.com",
                "1층",
                Collections.emptyList()
        );

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        //  When
        SessionResponse.Default response = sessionService.updateSession(session.getId(), request);

        //  Then
        assertNotNull(response);
        assertEquals(request.name(), response.name());
        assertEquals(request.shortDescription(), response.shortDescription());
        verify(imageService, times(1)).updateImages(any(ImageRequest.UploadImages.class));
    }

    @Test
    @DisplayName("[세션 삭제] 세션을 삭제하면 관련 이미지도 삭제되고, 세션 정보가 데이터베이스에서 제거되어야 한다.")
    void deleteSession_Success() {
        //  Given
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        //  When
        sessionService.deleteSession(session.getId());

        //  Then
        verify(imageService, times(1)).deleteImagesByEntity(session.getId(), EntityType.SESSION);
        verify(sessionRepository, times(1)).delete(session);
    }
}