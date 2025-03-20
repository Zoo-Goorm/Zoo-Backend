package zoo.insightnote.domain.insight.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import zoo.insightnote.domain.InsightLike.entity.QInsightLike;
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

@RequiredArgsConstructor
public class InsightQueryRepositoryImpl implements InsightQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Insight> findTopInsights() {
        QInsight insight = QInsight.insight;
        QInsightLike insightLike = QInsightLike.insightLike;

        return queryFactory
                .select(insight)
                .from(insight)
                .leftJoin(insightLike).on(insightLike.insight.eq(insight)) // 좋아요 개수 세기
                .groupBy(insight.id)
                .orderBy(insightLike.id.count().desc(), insight.createAt.desc()) // 좋아요 개수 내림차순 -> 최신순 정렬
                .limit(3)
                .fetch();
    }


    @Override
    public List<Insight> findInsightsByEventDay(LocalDate eventDay, Integer offset, Integer limit) {
        QInsight insight = QInsight.insight;
        QSession session = QSession.session;

        return queryFactory
                .select(insight)
                .from(insight)
                .join(insight.session, session)
                .where(
                        session.eventDay.eq(eventDay), // 특정 날짜에 시작하는 세션
                        insight.isDraft.eq(false) // 임시 저장된 인사이트 제외
                )
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