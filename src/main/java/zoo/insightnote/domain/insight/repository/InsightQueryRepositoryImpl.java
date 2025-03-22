package zoo.insightnote.domain.insight.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import zoo.insightnote.domain.InsightLike.entity.QInsightLike;
import zoo.insightnote.domain.comment.entity.QComment;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.QInsight;
import zoo.insightnote.domain.keyword.entity.QKeyword;
import zoo.insightnote.domain.session.entity.QSession;
import zoo.insightnote.domain.sessionKeyword.entity.QSessionKeyword;
import zoo.insightnote.domain.user.entity.QUser;
import zoo.insightnote.domain.userIntroductionLink.entity.QUserIntroductionLink;
import zoo.insightnote.domain.voteOption.entity.QVoteOption;
import zoo.insightnote.domain.voteResponse.entity.QVoteResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<InsightResponseDto.InsightListQueryDto> findInsightsByEventDay(
            LocalDate eventDay,
            Long sessionId,
            String sort,
            Pageable pageable
    ) {
        QInsight insight = QInsight.insight;
        QSession session = QSession.session;
        QInsightLike insightLike = QInsightLike.insightLike;
        QUser user = QUser.user;
        QComment comment = QComment.comment;

        OrderSpecifier<?> orderSpecifier = sort.equals("likes")
                ? insightLike.id.count().desc()
                : insight.createAt.desc();

        BooleanBuilder where = new BooleanBuilder()
                .and(session.eventDay.eq(eventDay))
                .and(insight.isDraft.isFalse());

        if (sessionId != null) {
            where.and(session.id.eq(sessionId));
        }
        
        List<InsightResponseDto.InsightListQueryDto> results = queryFactory
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
                        insightLike.id.countDistinct().as("likeCount"),
                        Expressions.stringTemplate(
                                "(SELECT i.fileUrl FROM Image i WHERE i.entityId = {0} AND i.entityType = 'INSIGHT' ORDER BY i.createAt DESC, i.id DESC LIMIT 1)",
                                insight.id
                        ),
                        user.interestCategory,
                        comment.id.countDistinct().as("commentCount")
                ))
                .from(insight)
                .join(insight.session, session)
                .leftJoin(insight.user, user)
                .leftJoin(insightLike).on(insightLike.insight.eq(insight))
                .leftJoin(comment).on(comment.insight.eq(insight))
                .where(where)
                .groupBy(insight.id, session.id, session.name, session.eventDay, user.interestCategory)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수
        Long totalCount = queryFactory
                .select(insight.count())
                .from(insight)
                .join(insight.session, session)
                .where(where)
                .fetchOne();

        return new PageImpl<>(results, pageable, totalCount == null ? 0 : totalCount);
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
        QVoteOption voteOption = QVoteOption.voteOption;
        QVoteResponse voteResponse = QVoteResponse.voteResponse;

        Long totalVotes = Optional.ofNullable(
                queryFactory
                        .select(voteResponse.id.count())
                        .from(voteResponse)
                        .where(voteResponse.voteOption.insight.id.eq(insightId))
                        .fetchOne()
        ).orElse(1L);

        // 1. 투표 옵션과 투표 수 조회
        List<Tuple> voteResults = queryFactory
                .select(voteOption.id, voteOption.optionText, voteResponse.id.count())
                .from(voteOption)
                .leftJoin(voteResponse).on(voteResponse.voteOption.eq(voteOption))
                .where(voteOption.insight.id.eq(insightId))
                .groupBy(voteOption.id)
                .fetch();

        List<InsightResponseDto.VoteOptionDto> voteOptions = voteResults.stream()
                .map(tuple -> {
                    Long voteCount = tuple.get(voteResponse.id.count());
                    double percentage = (voteCount == null ? 0 : (voteCount * 100.0 / totalVotes));
                    return new InsightResponseDto.VoteOptionDto(
                            tuple.get(voteOption.id),
                            tuple.get(voteOption.optionText),
                            String.format("%.1f%%", percentage)// 퍼센트 변환
                    );
                })
                .collect(Collectors.toList());


//        List<InsightResponseDto.VoteOptionDto> voteOptions = voteResults.stream()
//                .map(tuple -> new InsightResponseDto.VoteOptionDto(
//                        tuple.get(voteOption.id),
//                        tuple.get(voteOption.optionText),
//                        tuple.get(voteResponse.id.count()).intValue()))
//                .collect(Collectors.toList());

        // 2. 인사이트 상세 정보 조회
        InsightResponseDto.InsightDetailQueryDto insightDetail = queryFactory
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
                .fetchOne();

        // 3. 조회된 결과가 있다면 투표 정보 추가 후 반환
        return Optional.ofNullable(insightDetail)
                .map(detail -> {
                    detail.setVoteOptions(voteOptions);
                    return detail;
                });
    }
}