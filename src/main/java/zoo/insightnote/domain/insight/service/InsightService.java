package zoo.insightnote.domain.insight.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.InsightLike.entity.InsightLike;
import zoo.insightnote.domain.InsightLike.repository.InsightLikeRepository;
import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.dto.request.InsightCreateRequest;
import zoo.insightnote.domain.insight.dto.request.InsightUpdateRequest;
import zoo.insightnote.domain.insight.dto.response.InsightIdResponse;
import zoo.insightnote.domain.insight.dto.response.InsightListResponse;
import zoo.insightnote.domain.insight.dto.response.InsightTopListResponse;
import zoo.insightnote.domain.insight.dto.response.query.InsightListQuery;
import zoo.insightnote.domain.insight.dto.response.query.InsightTopListQuery;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.mapper.InsightListMapper;
import zoo.insightnote.domain.insight.mapper.InsightMapper;
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

import static zoo.insightnote.domain.insight.entity.QInsight.insight;

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
            throw new CustomException(ErrorCode.NO_EDIT_PERMISSION); // 예외 코드는 상황에 맞게 설정
        }

        insight.updateIfChanged(request);

        return new InsightIdResponse(insight.getId());
    }

    @Transactional(readOnly = true)
    public InsightResponseDto.SessionInsightListPageRes getInsightsBySession(Long sessionId, String sort, Pageable pageable, String userName) {

        User user = userService.findByUsername(userName);

        Page<InsightResponseDto.SessionInsightListQueryDto> insightPage = insightRepository.findInsightsBySessionId(sessionId, sort, pageable, user.getId());

        return InsightMapper.toSessionInsightPageResponse(insightPage, pageable.getPageNumber(), pageable.getPageSize());
    }


//    @Transactional
//    public InsightResponseDto.InsightIdRes updateInsightMemoOnly(Long insightId, String updatedMemo) {
//        Insight insight = insightRepository.findById(insightId)
//                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));
//
//        insight.updateMemoOnly(updatedMemo);
//        return InsightMapper.toResponse(insight);
//    }



    @Transactional
    public void saveVoteOptions(Insight insight, List<String> voteOptionTexts) {
        List<VoteOption> voteOptions = voteOptionTexts.stream()
                .map(optionText -> new VoteOption(insight, optionText))
                .collect(Collectors.toList());

        voteOptionRepository.saveAll(voteOptions);
    }

//    @Transactional
//    public InsightResponseDto.InsightRes updateInsight(Long insightId, InsightRequestDto.UpdateDto request) {
//        Insight insight = insightRepository.findById(insightId)
//                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));
//
//        insight.updateIfChanged(request.getMemo(), request.getIsPublic(), request.getIsAnonymous(),request.getIsDraft(), request.getVoteTitle());
//
//        imageService.updateImages(new ImageRequest.UploadImages(insight.getId(), EntityType.INSIGHT, request.getImages()));
//
//        return InsightMapper.toResponse(insight);
//    }

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
//    @Transactional(readOnly = true)
//    public List<InsightResponseDto.InsightTopRes> getTopPopularInsights(String username) {
//        User user = userService.findByUsername(username);
//        List<InsightResponseDto.InsightTopListQueryDto> topList = insightRepository.findTopInsights(user.getId());
//        return InsightMapper.toTopInsightList(topList);
//    }

    // 인기순위 상위 3개 가져오기
    @Transactional(readOnly = true)
    public List<InsightTopListResponse> getTopPopularInsights(User user) {
        List<InsightTopListQuery> topList = insightRepository.findTopInsights(user.getId());
        return topList.stream()
                .map(InsightTopListResponse::from)
                .collect(Collectors.toList());
    }


//    // 인사이트 목록
//    @Transactional(readOnly = true)
//    public InsightResponseDto.InsightListPageRes getInsightsByEventDay(LocalDate eventDay, Long sessionId, String sort, int page, User user) {
//
//        int pageSize = 3;  // 한 페이지당 9개
//        Pageable pageable = PageRequest.of(page, pageSize);
//        Page<InsightResponseDto.InsightListQueryDto> insightPage =
//                insightRepository.findInsightsByEventDay(eventDay, sessionId, sort, pageable, user.getId());
//
//        if (insightPage.isEmpty()) {
//            throw new CustomException(ErrorCode.INSIGHT_NOT_FOUND);
//        }
//
//        return InsightMapper.toListPageResponse(insightPage, page, pageSize);
//    }


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
    public InsightResponseDto.InsightDetailPageRes getInsightDetail(Long insightId , String username) {

        User user = userService.findByUsername(username);
        InsightResponseDto.InsightDetailQueryDto insightDto = insightRepository.findByIdWithSessionAndUser(insightId,user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        return InsightMapper.toDetailPageResponse(insightDto);
    }

    @Transactional(readOnly = true)
    public InsightResponseDto.MyInsightListPageRes getMyInsights(
            String username,
            LocalDate eventDay,
            Long sessionId,
            Pageable pageable
    ) {

        // 기존에는 DTO를 직접 반환했지만, 이제는 Page로 받아서 Mapper로 변환
        Page<InsightResponseDto.MyInsightListQueryDto> myInsightList =
                insightRepository.findMyInsights(username, eventDay, sessionId, pageable);

        if (myInsightList.isEmpty()) {
            throw new CustomException(ErrorCode.INSIGHT_NOT_FOUND);
        }


        return InsightMapper.toMyListPageResponse(myInsightList, pageable.getPageNumber(), pageable.getPageSize()
        );
    }

}
