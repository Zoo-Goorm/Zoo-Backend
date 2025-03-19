package zoo.insightnote.domain.insight.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import zoo.insightnote.domain.InsightLike.entity.QInsightLike;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.entity.QInsight;
import zoo.insightnote.domain.session.entity.QSession;

import java.time.LocalDate;
import java.util.List;

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
}