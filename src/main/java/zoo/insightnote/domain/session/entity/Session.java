package zoo.insightnote.domain.session.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

import lombok.*;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.session.dto.SessionRequest;
import zoo.insightnote.domain.speaker.entity.Speaker;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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
    private Integer eventDay;

    private String name;

    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String longDescription;

    private Integer maxCapacity;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    private String videoLink;

    private String location;

    public static Session create(SessionRequest.Create request, Event event, Speaker speaker) {
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

    public void update(SessionRequest.Update request) {
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
