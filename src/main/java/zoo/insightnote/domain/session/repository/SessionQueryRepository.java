package zoo.insightnote.domain.session.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
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
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class SessionQueryRepository {

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
        Map<String, List<SessionResponseDto.SessionAllRes>> groupedByDate = new LinkedHashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M월 d일");

        for (Tuple row : results) {
            LocalDateTime startTime = row.get(5, LocalDateTime.class);  // 첫 번째 쿼리에서 startTime 인덱스는 5
            String formattedDate = startTime.format(dateFormatter);

            groupedByDate
                    .computeIfAbsent(formattedDate, k -> new ArrayList<>())
                    .add(mapToSessionAllRes(row));
        }

        return groupedByDate;
    }

    private Map<String, List<SessionResponseDto.SessionDetailedRes>> processResultsForSessionDetailedRes(List<Tuple> results) {
        Map<String, List<SessionResponseDto.SessionDetailedRes>> groupedByDate = new LinkedHashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M월 d일");

        for (Tuple row : results) {
            LocalDateTime startTime = row.get(8, LocalDateTime.class);  // 두 번째 쿼리에서 startTime 인덱스는 8
            String formattedDate = startTime.format(dateFormatter);

            groupedByDate
                    .computeIfAbsent(formattedDate, k -> new ArrayList<>())
                    .add(mapToSessionDetailedRes(row));
        }

        return groupedByDate;
    }

    private SessionResponseDto.SessionAllRes mapToSessionAllRes(Tuple row) {
        LocalDateTime startTime = row.get(5, LocalDateTime.class);
        LocalDateTime endTime = row.get(6, LocalDateTime.class);
        String timeRange = formatTimeRange(startTime, endTime);

        return SessionResponseDto.SessionAllRes.builder()
                .id(row.get(0, Long.class))
                .name(row.get(1, String.class))
                .shortDescription(row.get(3, String.class))
                .location(row.get(4, String.class))
                .startTime(startTime)
                .endTime(endTime)
                .timeRange(timeRange)
                .keywords(convertToSet(row.get(2, String.class)))
                .build();
    }

    private SessionResponseDto.SessionDetailedRes mapToSessionDetailedRes(Tuple row) {
        LocalDateTime startTime = row.get(8, LocalDateTime.class);
        LocalDateTime endTime = row.get(9, LocalDateTime.class);
        String timeRange = formatTimeRange(startTime, endTime);
        SessionStatus status = row.get(10, SessionStatus.class);

        return SessionResponseDto.SessionDetailedRes.builder()
                .id(row.get(0, Long.class))
                .name(row.get(1, String.class))
                .shortDescription(row.get(2, String.class))
                .maxCapacity(row.get(3, Integer.class))
                .participantCount(row.get(4, Integer.class))
                .location(row.get(5, String.class))
                .speakerName(row.get(6, String.class))
                .speakerImageUrl(row.get(7, String.class))
                .startTime(startTime)
                .endTime(endTime)
                .status(status)
                .timeRange(timeRange)
                .keywords(new ArrayList<>(convertToSet(row.get(11, String.class))))
                .build();
    }

    private Set<String> convertToSet(String keywords) {
        return keywords != null ? new LinkedHashSet<>(List.of(keywords.split(","))) : Collections.emptySet();
    }

    private String formatTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(timeFormatter) + "~" + endTime.format(timeFormatter);
    }
}