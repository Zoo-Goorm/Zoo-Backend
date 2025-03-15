package zoo.insightnote.domain.session.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.career.entity.QCareer;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.entity.QSession;
import zoo.insightnote.domain.session.entity.SessionStatus;
import zoo.insightnote.domain.speaker.entity.QSpeaker;
import zoo.insightnote.domain.image.entity.QImage;
import zoo.insightnote.domain.keyword.entity.QKeyword;
import zoo.insightnote.domain.sessionKeyword.entity.QSessionKeyword;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static zoo.insightnote.domain.image.entity.QImage.image;

@Repository
@RequiredArgsConstructor
public class SessionCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 세션 목록 페이지
    public Map<String, List<SessionResponseDto.SessionAllRes>> findAllSessionsWithKeywords() {
        QSession session = QSession.session;
        QSessionKeyword sessionKeyword = QSessionKeyword.sessionKeyword;
        QKeyword keyword = QKeyword.keyword;

        Expression<String> keywordConcat = Expressions.stringTemplate(
                "function('group_concat', distinct {0})", keyword.name);

        List<Tuple> results = queryFactory
                .select(
                        session.id,
                        session.name,
                        keywordConcat,
                        session.shortDescription,
                        session.location,
                        session.startTime,
                        session.endTime
                )
                .from(session)
                .leftJoin(sessionKeyword).on(sessionKeyword.session.eq(session))
                .leftJoin(sessionKeyword.keyword, keyword)
                .groupBy(session.id, session.name, session.shortDescription, session.location, session.startTime, session.endTime)
                .orderBy(session.id.asc())
                .fetch();

        return processResultsForSessionAllRes(results);
    }


    // 세션 참가 페이지
    public Map<String, List<SessionResponseDto.SessionDetailedRes>> findAllSessionsWithDetails(EntityType entityType) {
        QSession session = QSession.session;
        QSpeaker speaker = QSpeaker.speaker;
        QImage image = QImage.image;
        QSessionKeyword sessionKeyword = QSessionKeyword.sessionKeyword;
        QKeyword keyword = QKeyword.keyword;

        Expression<String> keywordConcat = Expressions.stringTemplate(
                "function('group_concat', distinct {0})", keyword.name);

        List<Tuple> results = queryFactory
                .select(
                        session.id,
                        session.name,
                        session.shortDescription,
                        session.maxCapacity,
                        session.participantCount,
                        session.location,
                        speaker.name,
                        image.fileUrl,
                        session.startTime,
                        session.endTime,
                        session.status,
                        keywordConcat
                )
                .from(session)
                .join(session.speaker, speaker)
                .leftJoin(image).on(image.entityId.eq(speaker.id).and(image.entityType.eq(entityType)))
                .leftJoin(sessionKeyword).on(sessionKeyword.session.eq(session))
                .leftJoin(sessionKeyword.keyword, keyword)
                .groupBy(session.id, session.name, session.shortDescription, session.maxCapacity,
                        session.participantCount, session.location, speaker.name, image.fileUrl,
                        session.startTime, session.endTime, session.status)
                .orderBy(session.id.asc())
                .fetch();

        return processResultsForSessionDetailedRes(results);
    }

    private Map<String, List<SessionResponseDto.SessionAllRes>> processResultsForSessionAllRes(List<Tuple> results) {
        // 임시 그룹화 Map: 날짜 -> (timeRange -> 세션 리스트)
        Map<String, Map<String, List<SessionResponseDto.SessionAllRes.SessionDetail>>> tempGrouped = new LinkedHashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M월 d일");

        // 1. 세션 데이터를 날짜와 timeRange로 그룹화
        for (Tuple row : results) {
            LocalDateTime startTime = row.get(5, LocalDateTime.class);
            LocalDateTime endTime = row.get(6, LocalDateTime.class);
            String formattedDate = startTime.format(dateFormatter);
            String timeRange = formatTimeRange(startTime, endTime);

            // mapToSessionAllRes 사용
            SessionResponseDto.SessionAllRes.SessionDetail sessionDetail = mapToSessionAllRes(row, timeRange);

            tempGrouped
                    .computeIfAbsent(formattedDate, k -> new LinkedHashMap<>())
                    .computeIfAbsent(timeRange, k -> new ArrayList<>())
                    .add(sessionDetail);
        }

        // 2. 최종 결과로 변환 (timeRange로 묶기)
        Map<String, List<SessionResponseDto.SessionAllRes>> finalGrouped = new LinkedHashMap<>();

        for (Map.Entry<String, Map<String, List<SessionResponseDto.SessionAllRes.SessionDetail>>> dateEntry : tempGrouped.entrySet()) {
            List<SessionResponseDto.SessionAllRes> timeRangeList = new ArrayList<>();

            for (Map.Entry<String, List<SessionResponseDto.SessionAllRes.SessionDetail>> timeRangeEntry : dateEntry.getValue().entrySet()) {
                SessionResponseDto.SessionAllRes sessionRes = SessionResponseDto.SessionAllRes.builder()
                        .timeRange(timeRangeEntry.getKey())
                        .sessions(timeRangeEntry.getValue()) // 세션 리스트 추가
                        .build();

                timeRangeList.add(sessionRes);
            }

            finalGrouped.put(dateEntry.getKey(), timeRangeList);
        }

        return finalGrouped;
    }


    private Map<String, List<SessionResponseDto.SessionDetailedRes>> processResultsForSessionDetailedRes(List<Tuple> results) {
        Map<String, Map<String, List<SessionResponseDto.SessionDetailedRes.SessionDetail>>> tempGrouped = new LinkedHashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M월 d일");

        for (Tuple row : results) {
            LocalDateTime startTime = row.get(8, LocalDateTime.class);
            LocalDateTime endTime = row.get(9, LocalDateTime.class);
            String formattedDate = startTime.format(dateFormatter);

            String timeRange = formatTimeRange(startTime, endTime);
            SessionResponseDto.SessionDetailedRes.SessionDetail sessionDetail = mapToSessionDetail(row);

            // 날짜별, timeRange별로 그룹화
            tempGrouped
                    .computeIfAbsent(formattedDate, k -> new LinkedHashMap<>())
                    .computeIfAbsent(timeRange, k -> new ArrayList<>())
                    .add(sessionDetail);
        }

        // 최종 결과로 변환 (timeRange로 묶기)
        Map<String, List<SessionResponseDto.SessionDetailedRes>> finalGrouped = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, List<SessionResponseDto.SessionDetailedRes.SessionDetail>>> dateEntry : tempGrouped.entrySet()) {
            List<SessionResponseDto.SessionDetailedRes> timeRangeList = new ArrayList<>();

            for (Map.Entry<String, List<SessionResponseDto.SessionDetailedRes.SessionDetail>> timeRangeEntry : dateEntry.getValue().entrySet()) {

                List<SessionResponseDto.SessionDetailedRes.SessionDetail> sortedSessions = timeRangeEntry.getValue().stream()
                        .collect(Collectors.partitioningBy(session ->
                                session.getMaxCapacity() != null &&
                                        session.getParticipantCount() != null &&
                                        session.getMaxCapacity().equals(session.getParticipantCount())
                        ))
                        .values().stream()
                        .flatMap(List::stream)
                        .toList();

                SessionResponseDto.SessionDetailedRes detailedRes = SessionResponseDto.SessionDetailedRes.builder()
                        .timeRange(timeRangeEntry.getKey())
                        .sessions(sortedSessions)
                        .build();

                timeRangeList.add(detailedRes);
            }

            finalGrouped.put(dateEntry.getKey(), timeRangeList);
        }

        return finalGrouped;
    }

    private SessionResponseDto.SessionAllRes.SessionDetail mapToSessionAllRes(Tuple row, String timeRange) {
        return SessionResponseDto.SessionAllRes.SessionDetail.builder()
                .id(row.get(0, Long.class))
                .name(row.get(1, String.class))
                .shortDescription(row.get(3, String.class))
                .location(row.get(4, String.class))
                .startTime(row.get(5, LocalDateTime.class))
                .endTime(row.get(6, LocalDateTime.class))
                .timeRange(timeRange)
                .keywords(convertToSet(row.get(2, String.class)))
                .build();
    }

    private SessionResponseDto.SessionDetailedRes.SessionDetail mapToSessionDetail(Tuple row) {
        return SessionResponseDto.SessionDetailedRes.SessionDetail.builder()
                .id(row.get(0, Long.class))
                .name(row.get(1, String.class))
                .shortDescription(row.get(2, String.class))
                .maxCapacity(row.get(3, Integer.class))
                .participantCount(row.get(4, Integer.class))
                .location(row.get(5, String.class))
                .speakerName(row.get(6, String.class))
                .speakerImageUrl(row.get(7, String.class))
                .startTime(row.get(8, LocalDateTime.class))
                .endTime(row.get(9, LocalDateTime.class))
                .status(row.get(10, SessionStatus.class))
                .keywords(convertToSet(row.get(11, String.class)))
                .build();
    }

    private Set<String> convertToSet(String keywords) {
        return keywords != null ? new LinkedHashSet<>(List.of(keywords.split(","))) : Collections.emptySet();
    }

    private String formatTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(timeFormatter) + "~" + endTime.format(timeFormatter);
    }

    // 모달 페이지 전용 쿼리 (세션 정보 클릭시)
    public SessionResponseDto.SessionSpeakerDetailQueryDto findSessionAndSpeakerDetail(Long sessionId) {
        QSession session = QSession.session;
        QSpeaker speaker = QSpeaker.speaker;
        QSessionKeyword sessionKeyword = QSessionKeyword.sessionKeyword;
        QKeyword keyword = QKeyword.keyword;
        QCareer career = QCareer.career;
        QImage image = QImage.image;

        return queryFactory
                .select(Projections.constructor(
                        SessionResponseDto.SessionSpeakerDetailQueryDto.class,
                        session.name,
                        session.longDescription,
                        session.location,
                        session.maxCapacity,
                        session.participantCount,
                        speaker.name,
//                        Expressions.stringTemplate("group_concat({0}, {1})", keyword.name, Expressions.constant(",")),  // 쉼표 구분자
//                        Expressions.stringTemplate("group_concat({0}, {1})", career.description, Expressions.constant("||")), // '||' 구분자

                        Expressions.stringTemplate("group_concat(distinct {0})", keyword.name, Expressions.constant(",")),  // 쉼표 구분자
                        Expressions.stringTemplate("group_concat(distinct {0})", career.description), // '||' 구분자
                        Expressions.stringTemplate("MAX({0})", image.fileUrl)
                ))
                .from(session)
                .join(session.speaker, speaker)
                .leftJoin(sessionKeyword).on(sessionKeyword.session.eq(session))
                .leftJoin(sessionKeyword.keyword, keyword)
                .leftJoin(career).on(career.speaker.eq(speaker))
                .leftJoin(image).on(image.entityId.eq(speaker.id).and(image.entityType.eq(EntityType.SPEAKER)))
                .where(session.id.eq(sessionId))
                .fetchOne();
    }


}