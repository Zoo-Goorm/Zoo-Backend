package zoo.insightnote.domain.session.mapper;

import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.session.dto.SessionRequestDto;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.speaker.entity.Speaker;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class SessionMapper {

    public static Session toEntity(SessionRequestDto.Create request, Event event, Speaker speaker) {
        return Session.create(request, event, speaker);
    }

    // Session엔티티 객체를 dto에서 정의한 record르 변환
    public static SessionResponseDto.SessionRes toResponse(Session session, List<String> keywords) {
        return SessionResponseDto.SessionRes.builder()
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


    public static SessionResponseDto.SessionAllRes toAllResponse(Session session, List<String> keywords, String timeRange) {
        // 1. SessionDetail 생성
        SessionResponseDto.SessionAllRes.SessionDetail sessionDetail = SessionResponseDto.SessionAllRes.SessionDetail.builder()
                .id(session.getId())
                .name(session.getName())
                .shortDescription(session.getShortDescription())
                .location(session.getLocation())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .timeRange(timeRange)
                .keywords(new LinkedHashSet<>(keywords)) // Set으로 중복 제거
                .build();

        // 2. SessionAllRes 생성
        return SessionResponseDto.SessionAllRes.builder()
                .timeRange(timeRange)
                .sessions(List.of(sessionDetail))
                .build();
    }

    public static SessionResponseDto.SessionDetailedRes.SessionDetail toSessionDetail(
            Session session,
            String speakerImageUrl,
            int participantCount,
            List<String> keywords) {

        return SessionResponseDto.SessionDetailedRes.SessionDetail.builder()
                .id(session.getId())
                .name(session.getName())
                .shortDescription(session.getShortDescription())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .speakerName(session.getSpeaker().getName())
                .speakerImageUrl(speakerImageUrl)
                .maxCapacity(session.getMaxCapacity())
                .participantCount(participantCount)
                .keywords(new LinkedHashSet<>(keywords)) // Set으로 중복 제거
                .status(session.getStatus())
                .location(session.getLocation())
                .build();
    }



}
