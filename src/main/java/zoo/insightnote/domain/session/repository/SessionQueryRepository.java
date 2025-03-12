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
import zoo.insightnote.domain.speaker.entity.QSpeaker;
import zoo.insightnote.domain.image.entity.QImage;
import zoo.insightnote.domain.keyword.entity.QKeyword;
import zoo.insightnote.domain.sessionKeyword.entity.QSessionKeyword;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



@Repository
@RequiredArgsConstructor
public class SessionQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Map<String, List<SessionResponseDto.SessionDetailedRes>> findAllSessionsWithDetails(EntityType entityType) {
        QSession session = QSession.session;
        QSpeaker speaker = QSpeaker.speaker;
        QImage image = QImage.image;
        QSessionKeyword sessionKeyword = QSessionKeyword.sessionKeyword;
        QKeyword keyword = QKeyword.keyword;

        Expression<String> keywordConcat = Expressions.stringTemplate(
                "function('group_concat', {0})", keyword.name);

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
                .groupBy(
                        session.id, session.name, session.shortDescription, session.maxCapacity,
                        session.participantCount, session.location, speaker.name, image.fileUrl,
                        session.startTime, session.endTime, session.status
                )
                .orderBy(session.id.asc())
                .fetch();

        Map<String, List<SessionResponseDto.SessionDetailedRes>> groupedByDate = new LinkedHashMap<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M월 d일");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Tuple row : results) {
            LocalDateTime startTime = row.get(session.startTime);
            LocalDateTime endTime = row.get(session.endTime);
            String formattedDate = startTime.format(dateFormatter);
            String timeRange = startTime.format(timeFormatter) + "~" + endTime.format(timeFormatter);

            String keywordConcatResult = row.get(keywordConcat);
            List<String> keywordList = convertToList(keywordConcatResult);

            SessionResponseDto.SessionDetailedRes sessionRes = SessionResponseDto.SessionDetailedRes.builder()
                    .id(row.get(session.id))
                    .name(row.get(session.name))
                    .shortDescription(row.get(session.shortDescription))
                    .maxCapacity(row.get(session.maxCapacity))
                    .participantCount(row.get(session.participantCount))
                    .location(row.get(session.location))
                    .speakerName(row.get(speaker.name))
                    .speakerImageUrl(row.get(image.fileUrl))
                    .startTime(startTime)
                    .endTime(endTime)
                    .status(row.get(session.status))
                    .timeRange(timeRange)
                    .keywords(keywordList)
                    .build();

            groupedByDate.computeIfAbsent(formattedDate, k -> new ArrayList<>()).add(sessionRes);
        }

        return groupedByDate;
    }

    private static List<String> convertToList(String keywords) {
        return keywords != null ? List.of(keywords.split(",")) : Collections.emptyList();
    }
}