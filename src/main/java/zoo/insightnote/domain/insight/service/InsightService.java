package zoo.insightnote.domain.insight.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.InsightLike.entity.InsightLike;
import zoo.insightnote.domain.InsightLike.repository.InsightLikeRepository;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.dto.request.InsightCreateRequest;
import zoo.insightnote.domain.insight.dto.request.InsightUpdateRequest;
import zoo.insightnote.domain.insight.dto.response.*;
import zoo.insightnote.domain.insight.dto.response.query.*;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.mapper.*;
import zoo.insightnote.domain.insight.repository.InsightRepository;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.domain.user.service.UserService;
import zoo.insightnote.domain.voteOption.entity.VoteOption;
import zoo.insightnote.domain.voteOption.repository.VoteOptionRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsightService {

    private final InsightRepository insightRepository;
    private final InsightLikeRepository insightLikeRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final UserService userService;

    @Transactional
    public InsightIdResponse createInsight(InsightCreateRequest request , User user) {

        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        Insight insight = Insight.create(session, user, request);

        Insight savedInsight = insightRepository.save(insight);

        return new InsightIdResponse(savedInsight.getId());
    }

    @Transactional
    public InsightIdResponse updateInsight(Long insightId, InsightUpdateRequest request, User user) {

        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        if (!insight.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NO_EDIT_PERMISSION);
        }

        insight.updateIfChanged(request);

        return new InsightIdResponse(insight.getId());
    }

    // 특정 세션의 인사이트 목록
    @Transactional(readOnly = true)
    public SessionInsightListResponse getInsightsBySession(Long sessionId, String sort, Pageable pageable, String userName) {

        User user = userService.findByUsername(userName);

        Page<SessionInsightListQuery> insightPage = insightRepository.findInsightsBySessionId(sessionId, sort, pageable, user.getId());

        return SessionInsightListMapper.toSessionInsightListResponse(insightPage);
    }


    @Transactional
    public void saveVoteOptions(Insight insight, List<String> voteOptionTexts) {
        List<VoteOption> voteOptions = voteOptionTexts.stream()
                .map(optionText -> new VoteOption(insight, optionText))
                .collect(Collectors.toList());

        voteOptionRepository.saveAll(voteOptions);
    }

    @Transactional
    public void deleteInsight(Long insightId, User user) {
        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        if (!insight.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        insightRepository.delete(insight);
    }

    @Transactional(readOnly = true)
    public InsightResponseDto.InsightRes getInsightById(Long insightId) {
        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));
        return InsightMapper.toResponse(insight);
    }

    @Transactional
    public int toggleLike(User user, Long insightId) {

        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        if (insight.getUser().equals(user)) {
            throw new CustomException(ErrorCode.CANNOT_LIKE_OWN_INSIGHT);
        }

        // 이미 좋아요가 있는지 확인
        Optional<InsightLike> existingLike = insightLikeRepository.findByUserAndInsight(user, insight);

        if (existingLike.isPresent()) {
            // 이미 좋아요가 있다면 => 좋아요 취소
            insightLikeRepository.delete(existingLike.get());
            return -1;
        } else {
            InsightLike newLike = InsightLike.create(user, insight);
            insightLikeRepository.save(newLike);
            return 1;
        }
    }

    // 인기순위 상위 3개 가져오기
    @Transactional(readOnly = true)
    public List<InsightTopListResponse> getTopPopularInsights(User user) {
        List<InsightTopListQuery> topList = insightRepository.findTopInsights(user.getId());
        return topList.stream()
                .map(InsightTopListResponse::from)
                .collect(Collectors.toList());
    }

    // 인사이트 목록
    @Transactional(readOnly = true)
    public InsightListResponse getInsightsByEventDay(LocalDate eventDay, Long sessionId, String sort, int page, User user) {

        int pageSize = 3;  // 한 페이지당 9개
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<InsightListQuery> insightPage =
                insightRepository.findInsightsByEventDay(eventDay, sessionId, sort, pageable, user.getId());

        if (insightPage.isEmpty()) {
            throw new CustomException(ErrorCode.INSIGHT_NOT_FOUND);
        }

        return InsightListMapper.toListPageResponse(insightPage, page, pageSize);
    }


    // 인사이트 상세 페이지
    @Transactional(readOnly = true)
    public InsightDetailResponse getInsightDetail(Long insightId , String username) {

        User user = userService.findByUsername(username);
        InsightDetailQuery insightDto = insightRepository.findByIdWithSessionAndUser(insightId,user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        return InsightDetailMapper.toDetailPageResponse(insightDto);
    }

    //마이페이지 인사이트 목록
    @Transactional(readOnly = true)
    public MyInsightListResponse getMyInsights(
            String username,
            LocalDate eventDay,
            Long sessionId,
            Pageable pageable
    ) {
        Page<MyInsightListQuery> myInsightList = insightRepository.findMyInsights(username, eventDay, sessionId, pageable);
        if (myInsightList.isEmpty()) {
            throw new CustomException(ErrorCode.INSIGHT_NOT_FOUND);
        }

        return MyInsightListMapper.toMyListPageResponse(myInsightList, pageable.getPageNumber(), pageable.getPageSize());
    }

}
