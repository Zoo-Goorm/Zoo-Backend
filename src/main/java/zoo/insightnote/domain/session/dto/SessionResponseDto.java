package zoo.insightnote.domain.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.entity.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;


public class SessionResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SessionRes {
        private Long id;
        private String name;
        private String shortDescription;
        private String location;
        private Integer maxCapacity;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String speakerName;
        private List<String> keywords;
        private SessionStatus status;
    }

    // Entity -> DTO
    public static SessionRes toResponse(Session session, List<String> keywords) {
        return SessionRes.builder()
                .id(session.getId())
                .name(session.getName())
                .shortDescription(session.getShortDescription())
                .location(session.getLocation())
                .maxCapacity(session.getMaxCapacity())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .speakerName(session.getSpeaker().getName())
                .keywords(keywords)
                .status(session.getStatus())
                .build();
    }
}