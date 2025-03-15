package zoo.insightnote.domain.session.mapper;

import com.querydsl.core.Tuple;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.session.dto.SessionRequestDto;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.speaker.entity.Speaker;

import java.util.*;
import java.util.stream.Collectors;

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


    public static SessionResponseDto.SessionSpeakerDetailRes toSessionSpeakerDetailRes(SessionResponseDto.SessionSpeakerDetailQueryDto result) {

//        해당 주석은 중복 이슈를 팀원간 공유를 하고 삭제하거나 수정할 부분입니다
//        List<String> keywords = Optional.ofNullable(result.getKeywords())
//                .map(k -> List.of(k.split(",")))
//                .orElse(Collections.emptyList());
//
//        List<String> careers = Optional.ofNullable(result.getCareers())
//                .map(c -> List.of(c.split(",")))
//                .orElse(Collections.emptyList());

        // keywords 처리 (빈 문자열 제거)
        List<String> keywords = Optional.ofNullable(result.getKeywords())
                .map(k -> Arrays.stream(k.split(","))
                        .filter(s -> !s.isBlank())  // 빈 문자열 제거
                        .distinct()                // 중복 제거
                        .toList())
                .orElse(Collections.emptyList());

        System.out.println(result.getCareers());
        List<String> careers = Optional.ofNullable(result.getCareers())
                .map(c -> Arrays.stream(c.split("\\|\\|"))
                        .map(String::trim)                          // 공백 제거
                        .map(s -> s.startsWith(",") ? s.substring(1).trim() : s) // 쉼표로 시작하면 제거
                        .filter(s -> !s.isBlank())                   // 빈 문자열 제거
                        .collect(Collectors.toCollection(LinkedHashSet::new))) // 중복 제거 및 순서 유지
                .map(ArrayList::new)                                 // Set을 List로 변환
                .orElseGet(ArrayList::new);                          // 빈 리스트 처리

        return SessionResponseDto.SessionSpeakerDetailRes.builder()
                .sessionName(result.getSessionName())
                .longDescription(result.getLongDescription())
                .location(result.getLocation())
                .maxCapacity(result.getMaxCapacity())
                .participantCount(result.getParticipantCount())
                .speakerName(result.getSpeakerName())
                .keywords(keywords)
                .careers(careers)
                .build();
    }
}
