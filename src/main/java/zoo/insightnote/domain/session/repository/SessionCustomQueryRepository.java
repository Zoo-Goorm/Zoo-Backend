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
import zoo.insightnote.domain.session.dto.response.query.SessionSpeakerDetailQuery;
import zoo.insightnote.domain.session.entity.QSession;
import zoo.insightnote.domain.speaker.entity.QSpeaker;
import zoo.insightnote.domain.image.entity.QImage;
import zoo.insightnote.domain.keyword.entity.QKeyword;
import zoo.insightnote.domain.sessionKeyword.entity.QSessionKeyword;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class SessionCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 세션 목록 페이지
    public List<Tuple> findAllSessionsWithKeywords() {
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

        return results;
    }


    // 세션 참가 페이지
    public List<Tuple> findAllSessionsWithDetails(EntityType entityType) {
        QSession session = QSession.session;
        QSpeaker speaker = QSpeaker.speaker;
        QImage image = QImage.image;
        QSessionKeyword sessionKeyword = QSessionKeyword.sessionKeyword;
        QKeyword keyword = QKeyword.keyword;

        Expression<String> keywordConcat = Expressions.stringTemplate(
                "function('group_concat', distinct {0})", keyword.name);


        return queryFactory
                .select(
                        session.id,
                        session.name,
                        keywordConcat,
                        session.shortDescription,
                        session.location,
                        session.startTime,
                        session.endTime,
                        session.participantCount,
                        session.maxCapacity,
                        speaker.name,
                        image.fileUrl
                )
                .from(session)
                .join(session.speaker, speaker)
                .leftJoin(image).on(image.entityId.eq(speaker.id).and(image.entityType.eq(entityType)))
                .leftJoin(sessionKeyword).on(sessionKeyword.session.eq(session))
                .leftJoin(sessionKeyword.keyword, keyword)
                .groupBy(session.id, session.name, session.shortDescription, session.location,
                        session.startTime, session.endTime, session.participantCount,
                        session.maxCapacity, speaker.name, image.fileUrl)
                .orderBy(session.id.asc())
                .fetch();
    }




    // 모달 페이지 전용 쿼리 (세션 정보 클릭시)
    public SessionSpeakerDetailQuery findSessionAndSpeakerDetail(Long sessionId) {
        QSession session = QSession.session;
        QSpeaker speaker = QSpeaker.speaker;
        QSessionKeyword sessionKeyword = QSessionKeyword.sessionKeyword;
        QKeyword keyword = QKeyword.keyword;
        QCareer career = QCareer.career;
        QImage image = QImage.image;

        return queryFactory
                .select(Projections.constructor(
                        SessionSpeakerDetailQuery.class,
                        session.name,
                        session.longDescription,
                        session.location,
                        session.maxCapacity,
                        session.participantCount,
                        speaker.name,
                        Expressions.stringTemplate("group_concat(distinct {0})", keyword.name, Expressions.constant(",")),  // 쉼표 구분자
                        Expressions.stringTemplate("group_concat(distinct {0})", career.description), // '||' 구분자
                        Expressions.stringTemplate("MAX({0})", image.fileUrl)

                ))
                .from(session)
                .join(session.speaker, speaker)
                .leftJoin(sessionKeyword).on(sessionKeyword.session.eq(session))
                .leftJoin(sessionKeyword.keyword, keyword)
                .leftJoin(career).on(career.speaker.eq(speaker))
                .leftJoin(image)
                    .on(image.entityId.eq(speaker.id).and(image.entityType.stringValue().eq("SPEAKER")))
                .where(session.id.eq(sessionId))
                .fetchOne();
    }


}