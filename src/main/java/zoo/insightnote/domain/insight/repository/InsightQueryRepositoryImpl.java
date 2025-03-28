package zoo.insightnote.domain.insight.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import zoo.insightnote.domain.InsightLike.entity.QInsightLike;
import zoo.insightnote.domain.InsightLike.repository.InsightLikeRepository;
import zoo.insightnote.domain.comment.entity.QComment;
import zoo.insightnote.domain.comment.repository.CommentRepository;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.dto.response.query.InsightListQuery;
import zoo.insightnote.domain.insight.dto.response.query.InsightTopListQuery;
import zoo.insightnote.domain.insight.entity.QInsight;
import zoo.insightnote.domain.keyword.entity.QKeyword;
import zoo.insightnote.domain.session.entity.QSession;
import zoo.insightnote.domain.sessionKeyword.entity.QSessionKeyword;
import zoo.insightnote.domain.user.entity.QUser;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.userIntroductionLink.entity.QUserIntroductionLink;
import zoo.insightnote.domain.voteOption.entity.QVoteOption;
import zoo.insightnote.domain.voteResponse.entity.QVoteResponse;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static zoo.insightnote.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class InsightQueryRepositoryImpl implements InsightQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final InsightLikeRepository insightLikeRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<InsightTopListQuery> findTopInsights(Long userId) {
        QInsight insight = QInsight.insight;
        QInsightLike insightLike = QInsightLike.insightLike;
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        StringExpression displayNameExpr = new CaseBuilder()
                .when(insight.isAnonymous.isTrue()).then(user.nickname)
                .otherwise(user.name);

        List< InsightTopListQuery> result = queryFactory
                .select(Projections.constructor(
                        InsightTopListQuery.class,
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
                        ).as("imageUrl"),
                        comment.id.countDistinct().as("commentCount"),
                        displayNameExpr,
                        user.job,
                        user.interestCategory
                ))
                .from(insight)
                .leftJoin(insight.user, user)
                .leftJoin(insightLike).on(insightLike.insight.eq(insight))
                .leftJoin(comment).on(comment.insight.eq(insight))
                .where(
                        insight.isPublic.isTrue()
                                .and(insight.isDraft.isFalse())
                )
                .groupBy(insight.id, user.nickname, user.name, user.job, insight.isAnonymous, user.interestCategory)
                .orderBy(insightLike.id.count().desc(), insight.createAt.desc())
                .limit(3)
                .fetch();

        // 2. 성능 최적화된 좋아요 여부 처리
        if (userId != null && !result.isEmpty()) {
            List<Long> insightIds = result.stream()
                    .map(InsightTopListQuery::getId)
                    .collect(Collectors.toList());

            List<Long> likedInsightIds = queryFactory
                    .select(insightLike.insight.id)
                    .from(insightLike)
                    .where(insightLike.user.id.eq(userId)
                            .and(insightLike.insight.id.in(insightIds)))
                    .fetch();

            Set<Long> likedSet = new HashSet<>(likedInsightIds);

            result.forEach(dto -> dto.setIsLiked(likedSet.contains(dto.getId())));
        }

        return result;
    }


    @Override
    public Page<InsightListQuery> findInsightsByEventDay(
            LocalDate eventDay,
            Long sessionId,
            String sort,
            Pageable pageable,
            Long userId
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
                .and(insight.isDraft.isFalse())
                .and(insight.isPublic.isTrue());

        if (eventDay != null) {
            where.and(session.eventDay.eq(eventDay));
        }

        if (sessionId != null) {
            where.and(session.id.eq(sessionId));
        }

        StringExpression displayNameExpr = new CaseBuilder()
                .when(insight.isAnonymous.isTrue()).then(user.nickname)
                .otherwise(user.name);


        List<InsightListQuery> results = queryFactory
                .select(Projections.constructor(
                        InsightListQuery.class,
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
                        comment.id.countDistinct().as("commentCount"),
                        displayNameExpr,
                        user.job

                ))
                .from(insight)
                .join(insight.session, session)
                .leftJoin(insight.user, user)
                .leftJoin(insightLike).on(insightLike.insight.eq(insight))
                .leftJoin(comment).on(comment.insight.eq(insight))
                .where(where)
                .groupBy(insight.id, session.id, session.name, session.eventDay, user.interestCategory, user.nickname, user.name,user.job, insight.isAnonymous)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (userId != null && !results.isEmpty()) {
            List<Long> insightIds = results.stream()
                    .map(InsightListQuery::getId)
                    .collect(Collectors.toList());

            List<Long> likedInsightIds = queryFactory
                    .select(insightLike.insight.id)
                    .from(insightLike)
                    .where(insightLike.user.id.eq(userId)
                            .and(insightLike.insight.id.in(insightIds)))
                    .fetch();

            Set<Long> likedSet = new HashSet<>(likedInsightIds);

            results.forEach(dto -> dto.setIsLiked(likedSet.contains(dto.getId())));
        }

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
    public Optional<InsightResponseDto.InsightDetailQueryDto> findByIdWithSessionAndUser(Long insightId , Long userId ) {
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


        boolean isLiked = insightLikeRepository.existsByUserIdAndInsightId(userId, insightId);

        long speakerCommentCount = commentRepository.countSpeakerCommentsOnInsight(insightId);
        boolean hasSpeakerComment = speakerCommentCount > 0;


        // 3. 조회된 결과가 있다면 투표 정보 추가 후 반환
        return Optional.ofNullable(insightDetail)
                .map(detail -> {
                    detail.setVoteOptions(voteOptions);
                    detail.setIsLiked(isLiked);
                    detail.setHasSpeakerComment(hasSpeakerComment);
                    return detail;
                });
    }

    @Override
    public Page<InsightResponseDto.SessionInsightListQueryDto> findInsightsBySessionId(
            Long sessionId, String sort, Pageable pageable, Long currentUserId
    ) {
        QInsight insight = QInsight.insight;
        QInsightLike insightLike = QInsightLike.insightLike;
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        // 정렬 조건 설정
        OrderSpecifier<?> dynamicSort = sort.equals("likes")
                ? insightLike.id.countDistinct().desc()
                : insight.createAt.desc();

        BooleanBuilder where = new BooleanBuilder()
                .and(insight.session.id.eq(sessionId))
                .and(
                        insight.isDraft.isFalse()
                                .or(
                                        insight.isDraft.isTrue().and(
                                                currentUserId != null
                                                        ? insight.user.id.eq(currentUserId)
                                                        : insight.user.id.isNull()
                                        )
                                )
                );

        StringExpression displayNameExpr = new CaseBuilder()
                .when(insight.isAnonymous.isTrue()).then(user.nickname)
                .otherwise(user.name);

        List<InsightResponseDto.SessionInsightListQueryDto> results = queryFactory
                .select(Projections.constructor(
                        InsightResponseDto.SessionInsightListQueryDto.class,
                        insight.id,
                        insight.memo,
                        insight.isPublic,
                        insight.isAnonymous,
                        insight.createAt,
                        insight.updatedAt,
                        insightLike.id.countDistinct().as("likeCount"),
                        comment.id.countDistinct().as("commentCount"),
                        displayNameExpr,
                        user.job
                ))
                .from(insight)
                .leftJoin(insight.user, user)
                .leftJoin(insightLike).on(insightLike.insight.eq(insight))
                .leftJoin(comment).on(comment.insight.eq(insight))
                .where(where)
                .groupBy(insight.id, user.nickname, user.name, user.job, insight.isAnonymous)
                .orderBy(insight.isDraft.desc(), dynamicSort) // 1. 임시저장 먼저, 2. 정렬조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //  좋아요 여부 in-query로 최적화 처리
        if (currentUserId != null && !results.isEmpty()) {
            List<Long> insightIds = results.stream()
                    .map(InsightResponseDto.SessionInsightListQueryDto::getId)
                    .toList();

            List<Long> likedIds = insightLikeRepository.findInsightIdsByUserIdAndInsightIds(currentUserId, insightIds);
            Set<Long> likedIdSet = new HashSet<>(likedIds);

            results.forEach(dto -> dto.setIsLiked(likedIdSet.contains(dto.getId())));
        }

        // 연사 댓글 여부
        if (!results.isEmpty()) {
            List<Long> insightIds = results.stream()
                    .map(InsightResponseDto.SessionInsightListQueryDto::getId)
                    .toList();

            List<Long> speakerCommentInsightIds =
                    commentRepository.findInsightIdsWithSpeakerComments(insightIds); // 이 쿼리는 아래 참고!

            Set<Long> speakerInsightSet = new HashSet<>(speakerCommentInsightIds);

            results.forEach(dto ->
                    dto.setHasSpeakerComment(speakerInsightSet.contains(dto.getId()))
            );
        }

        // 전체 수
        Long total = queryFactory
                .select(insight.count())
                .from(insight)
                .where(where)
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }

    public Page<InsightResponseDto.MyInsightListQueryDto> findMyInsights(
            String username,
            LocalDate eventDay,
            Long sessionId,
            Pageable pageable
    ) {
        QInsight insight = QInsight.insight;
        QUser user = QUser.user;
        QSession session = QSession.session;

        BooleanBuilder where = new BooleanBuilder();
        where.and(insight.user.username.eq(username));

        if (eventDay != null) {
            where.and(session.eventDay.eq(eventDay));
        }

        if (sessionId != null) {
            where.and(session.id.eq(sessionId));
        }

        OrderSpecifier<?> orderSpecifier = insight.updatedAt.desc();
//        OrderSpecifier<?> orderSpecifier = sort.equals("likes")
//                ? insight.id.desc() // 임시 정렬 (likes 기준 정렬이 필요하다면 별도 countJoin 필요)
//                : insight.updatedAt.desc();

        List<InsightResponseDto.MyInsightListQueryDto> content = queryFactory
                .select(Projections.constructor(
                        InsightResponseDto.MyInsightListQueryDto.class,
                        insight.id,
                        insight.memo,
                        insight.isPublic,
                        insight.isAnonymous,
                        insight.isDraft,
                        insight.updatedAt,
                        session.id,
                        session.name
                ))
                .from(insight)
                .join(insight.session, session)
                .leftJoin(insight.user, user)
                .where(where)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트
        Long totalCount = queryFactory
                .select(insight.count())
                .from(insight)
                .join(insight.session, session)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount == null ? 0 : totalCount);
    }

}