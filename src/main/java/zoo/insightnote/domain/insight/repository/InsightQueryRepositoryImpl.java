package zoo.insightnote.domain.insight.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import zoo.insightnote.domain.InsightLike.entity.QInsightLike;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.QInsight;
import zoo.insightnote.domain.keyword.entity.QKeyword;
import zoo.insightnote.domain.session.entity.QSession;
import zoo.insightnote.domain.sessionKeyword.entity.QSessionKeyword;
import zoo.insightnote.domain.user.entity.QUser;
import zoo.insightnote.domain.userIntroductionLink.entity.QUserIntroductionLink;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
                        insightLike.id.count(),
                        Expressions.stringTemplate(
                                "(SELECT i.fileUrl FROM Image i " +
                                        "WHERE i.entityId = {0} AND i.entityType = 'INSIGHT' " +
                                        "ORDER BY i.createAt DESC LIMIT 1)",
                                insight.id
                        ).as("imageUrl")
                ))
                .from(insight)
                .leftJoin(insightLike).on(insightLike.insight.eq(insight))
                .groupBy(insight.id)
                .orderBy(insightLike.id.count().desc(), insight.createAt.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public Page<InsightResponseDto.InsightListQueryDto> findInsightsByEventDay(LocalDate eventDay, Pageable pageable) {
        QInsight insight = QInsight.insight;
        QSession session = QSession.session;
        QInsightLike insightLike = QInsightLike.insightLike;
        QUser user = QUser.user;

        // 데이터 조회
        List<InsightResponseDto.InsightListQueryDto> insights = queryFactory
                .select(Projections.constructor(
                        InsightResponseDto.InsightListQueryDto.class,
                        insight.id,
                        insight.memo,
                        insight.isPublic,
                        insight.isAnonymous,
                        insight.createAt,
                        insight.updatedAt,
                        session.id,
                        session.name,
                        insightLike.id.count(), // 좋아요 개수
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
                .leftJoin(insightLike).on(insightLike.insight.eq(insight))
                .leftJoin(insight.user, user)
                .where(
                        session.eventDay.eq(eventDay),
                        insight.isDraft.eq(false)
                )
                .groupBy(insight.id, session.id, session.name, session.eventDay)
                .orderBy(insight.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalElements = Optional.ofNullable(
                queryFactory
                        .select(insight.count())
                        .from(insight)
                        .join(insight.session, session)
                        .where(
                                session.eventDay.eq(eventDay),
                                insight.isDraft.eq(false)
                        )
                        .fetchFirst()
        ).orElse(0L);

        return new PageImpl<>(insights, pageable, totalElements);
    }

    @Override
    public Optional<InsightResponseDto.InsightDetailQueryDto> findByIdWithSessionAndUser(Long insightId) {
        QInsight insight = QInsight.insight;
        QSession session = QSession.session;
        QUser user = QUser.user;
        QSessionKeyword sessionKeyword = QSessionKeyword.sessionKeyword;
        QKeyword keyword = QKeyword.keyword;
        QUserIntroductionLink introductionLink = QUserIntroductionLink.userIntroductionLink;
        QInsightLike insightLike = QInsightLike.insightLike;

        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(
                        InsightResponseDto.InsightDetailQueryDto.class,
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