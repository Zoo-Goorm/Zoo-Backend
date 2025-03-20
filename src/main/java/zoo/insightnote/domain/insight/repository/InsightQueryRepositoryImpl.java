package zoo.insightnote.domain.insight.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import zoo.insightnote.domain.InsightLike.entity.QInsightLike;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.entity.QImage;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.entity.QInsight;
import zoo.insightnote.domain.keyword.entity.QKeyword;
import zoo.insightnote.domain.session.entity.QSession;
import zoo.insightnote.domain.sessionKeyword.entity.QSessionKeyword;
import zoo.insightnote.domain.user.entity.QUser;
import zoo.insightnote.domain.userIntroductionLink.entity.QUserIntroductionLink;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static zoo.insightnote.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class InsightQueryRepositoryImpl implements InsightQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<InsightResponseDto.InsightTopRes> findTopInsights() {
        QInsight insight = QInsight.insight;
        QInsightLike insightLike = QInsightLike.insightLike;

        return queryFactory
                .select(Projections.constructor(
                        InsightResponseDto.InsightTopRes.class,
                        insight.id,
                        insight.memo,
                        insight.isPublic,
                        insight.isAnonymous,
                        insight.createAt,
                        insight.updatedAt,
                        insightLike.id.count(), // 좋아요 개수
                        Expressions.stringTemplate(  // 최신 이미지 URL 가져오기
                                "(SELECT i.fileUrl FROM Image i " +
                                        "WHERE i.entityId = {0} AND i.entityType = 'INSIGHT' " +
                                        "ORDER BY i.createAt DESC LIMIT 1)",
                                insight.id
                        ).as("imageUrl") // 서브쿼리 활용하여 최신 이미지 1개 가져오기
                ))
                .from(insight)
                .leftJoin(insightLike).on(insightLike.insight.eq(insight))
                .groupBy(insight.id)
                .orderBy(insightLike.id.count().desc(), insight.createAt.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<InsightResponseDto.InsightByEventDayRes> findInsightsByEventDay(LocalDate eventDay, Integer offset, Integer limit) {
        QInsight insight = QInsight.insight;
        QSession session = QSession.session;
        QInsightLike insightLike = QInsightLike.insightLike;
//        QImage image = QImage.image; // 이미지 테이블 추가
        QUser user = QUser.user;

        return queryFactory
                .select(Projections.constructor(
                        InsightResponseDto.InsightByEventDayRes.class,
                        insight.id,
                        insight.memo,
                        insight.isPublic,
                        insight.isAnonymous,
                        insight.createAt,
                        insight.updatedAt,
                        session.id,
                        session.name,
                        insightLike.id.count(),// 좋아요 개수

                        //  최신 이미지 URL 가져오기 (서브쿼리)
                        Expressions.stringTemplate(
                                "(SELECT i.fileUrl FROM Image i " +
                                        "WHERE i.entityId = {0} AND i.entityType = 'INSIGHT' " +
                                        "ORDER BY i.createAt DESC, i.id DESC LIMIT 1)",
                                insight.id
                        ).as("imageUrl"),

                        user.interestCategory
                ))
                .from(insight)
                .join(insight.session, session)
                .leftJoin(insightLike).on(insightLike.insight.eq(insight)) // 좋아요 카운트 추가
                .leftJoin(insight.user, user)
                .where(
                        session.eventDay.eq(eventDay), // 특정 날짜에 시작하는 세션
                        insight.isDraft.eq(false) // 임시 저장된 인사이트 제외
                )
                .groupBy(insight.id, session.id, session.name, session.eventDay) //
                .orderBy(insight.createAt.desc()) // 최신순 정렬
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public Optional<InsightResponseDto.InsightWithDetailsQueryDto> findByIdWithSessionAndUser(Long insightId) {
        QInsight insight = QInsight.insight;
        QSession session = QSession.session;
        QUser user = QUser.user;
        QSessionKeyword sessionKeyword = QSessionKeyword.sessionKeyword;
        QKeyword keyword = QKeyword.keyword;
        QUserIntroductionLink introductionLink = QUserIntroductionLink.userIntroductionLink;
        QInsightLike insightLike = QInsightLike.insightLike;

        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(
                        InsightResponseDto.InsightWithDetailsQueryDto.class,
                        insight.id,
                        insight.memo,
                        insight.voteTitle,
                        insight.isPublic,
                        insight.isAnonymous,
                        insight.isDraft,
                        session.id,
                        session.name,
                        session.shortDescription,
                        user.id,
                        user.name,
                        user.email,
                        user.interestCategory,
                        Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", keyword.name),
                        Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", introductionLink.linkUrl),
                        insightLike.id.countDistinct()
                ))
                .from(insight)
                .join(insight.session, session)
                .join(insight.user, user)
                .leftJoin(sessionKeyword).on(sessionKeyword.session.eq(session))
                .leftJoin(sessionKeyword.keyword, keyword)
                .leftJoin(introductionLink).on(introductionLink.user.eq(user))
                .leftJoin(insightLike).on(insightLike.insight.eq(insight))
                .where(insight.id.eq(insightId))
                .groupBy(insight.id, session.id, user.id)
                .fetchOne());
    }

}