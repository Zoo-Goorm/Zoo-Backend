package zoo.insightnote.domain.session.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import zoo.insightnote.domain.event.entity.Event;

import zoo.insightnote.domain.session.dto.request.SessionCreateRequest;
import zoo.insightnote.domain.session.dto.request.SessionUpdateRequest;
import zoo.insightnote.domain.speaker.entity.Speaker;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id", nullable = false)
    private Speaker speaker;  // 연사 ID (FK)

    @Column(nullable = false)
    private LocalDate eventDay;

    private String name;

    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String longDescription;

    private Integer maxCapacity;

    // 세션 참가자 count용
    private Integer participantCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    private String videoLink;

    private String location;

    @Builder
    public Session(Event event, Speaker speaker, LocalDate eventDay, String name, String shortDescription,
                   String longDescription, Integer maxCapacity, Integer participantCount, LocalDateTime startTime,
                   LocalDateTime endTime, SessionStatus status, String videoLink, String location) {
        this.event = event;
        this.speaker = speaker;
        this.eventDay = eventDay;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.maxCapacity = maxCapacity;
        this.participantCount = participantCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.videoLink = videoLink;
        this.location = location;
    }


    public static Session create(SessionCreateRequest request, Event event, Speaker speaker) {
        return Session.builder()
                .event(event)
                .speaker(speaker)
                .eventDay(request.eventDay())
                .name(request.name())
                .shortDescription(request.shortDescription())
                .longDescription(request.longDescription())
                .maxCapacity(request.maxCapacity())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .status(request.status())
                .videoLink(request.videoLink())
                .location(request.location())
                .build();
    }

    public void update(SessionUpdateRequest request) {
        if (request.name() != null) this.name = request.name();
        if (request.shortDescription() != null) this.shortDescription = request.shortDescription();
        if (request.longDescription() != null) this.longDescription = request.longDescription();
        if (request.maxCapacity() != null) this.maxCapacity = request.maxCapacity();
        if (request.startTime() != null) this.startTime = request.startTime();
        if (request.endTime() != null) this.endTime = request.endTime();
        if (request.status() != null) this.status = request.status();
        if (request.videoLink() != null) this.videoLink = request.videoLink();
        if (request.location() != null) this.location = request.location();
    }
}
