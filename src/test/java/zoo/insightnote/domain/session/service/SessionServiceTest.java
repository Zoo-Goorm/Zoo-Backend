package zoo.insightnote.domain.session.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.service.EventService;
import zoo.insightnote.domain.image.service.ImageService;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.entity.SessionStatus;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.speaker.entity.Speaker;
import zoo.insightnote.domain.speaker.service.SpeakerService;

import java.time.LocalDateTime;

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


}