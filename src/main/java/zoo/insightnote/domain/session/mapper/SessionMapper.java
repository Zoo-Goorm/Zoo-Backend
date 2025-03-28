package zoo.insightnote.domain.session.mapper;

import com.querydsl.core.Tuple;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.session.dto.SessionRequestDto;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.speaker.entity.Speaker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SessionMapper {

//    public static Session toEntity(SessionRequestDto.Create request, Event event, Speaker speaker) {
//        return Session.create(request, event, speaker);
//    }


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


    public static SessionResponseDto.SessionSpeakerDetailRes toSessionSpeakerDetailRes(SessionResponseDto.SessionSpeakerDetailQueryDto result) {

        List<String> keywords = Optional.ofNullable(result.getKeywords())
                .map(k -> Arrays.stream(k.split(","))
                        .filter(s -> !s.isBlank())  // 빈 문자열 제거
                        .distinct()                // 중복 제거
                        .toList())
                .orElse(Collections.emptyList());

        List<String> careers = Optional.ofNullable(result.getCareers())
                .map(c -> c.replace(", ", "##SEP##"))
                .map(c -> Arrays.stream(c.split(","))
                        .map(s -> s.replace("##SEP##", ", "))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .distinct()
                        .toList())
                .orElseGet(ArrayList::new);


        return SessionResponseDto.SessionSpeakerDetailRes.builder()
                .sessionName(result.getSessionName())
                .longDescription(result.getLongDescription())
                .location(result.getLocation())
                .maxCapacity(result.getMaxCapacity())
                .participantCount(result.getParticipantCount())
                .speakerName(result.getSpeakerName())
                .keywords(keywords)
                .careers(careers)
                .imageUrl(result.getImageUrl())
                .build();
    }
}
