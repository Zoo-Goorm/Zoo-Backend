package zoo.insightnote.domain.session.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.session.dto.SessionRequestDto;
import zoo.insightnote.domain.session.dto.request.SessionCreateRequest;
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

    public static Session create(SessionCreateRequest request, Event event, Speaker speaker) {
        return Session.builder()
                .event(event)
                .speaker(speaker)
                .eventDay(request.getEventDay())
                .name(request.getName())
                .shortDescription(request.getShortDescription())
                .longDescription(request.getLongDescription())
                .maxCapacity(request.getMaxCapacity())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(request.getStatus())
                .videoLink(request.getVideoLink())
                .location(request.getLocation())
                .build();
    }

    public void update(SessionRequestDto.Update request) {
        if (request.getName() != null) this.name = request.getName();
        if (request.getShortDescription() != null) this.shortDescription = request.getShortDescription();
        if (request.getLongDescription() != null) this.longDescription = request.getLongDescription();
        if (request.getMaxCapacity() != null) this.maxCapacity = request.getMaxCapacity();
        if (request.getStartTime() != null) this.startTime = request.getStartTime();
        if (request.getEndTime() != null) this.endTime = request.getEndTime();
        if (request.getStatus() != null) this.status = request.getStatus();
        if (request.getVideoLink() != null) this.videoLink = request.getVideoLink();
        if (request.getLocation() != null) this.location = request.getLocation();
    }
}
