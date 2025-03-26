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
import zoo.insightnote.domain.insight.entity.Insight;
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
    public InsightResponseDto.InsightIdRes createInsight(InsightRequestDto.CreateInsight request ,String userName) {
        // 이 부분 스크링 시큐리티가 loadUserByUsername인증할때 쿼리  1번 + 아래 findByUsername 쿼리 1번
        // 총 2번 같은 쿼리가 실행됨 - 영광님 의견 필요
        User user = userService.findByUsername(userName);

        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        Insight insight = Insight.create(session, user, request);

        Insight savedInsight = insightRepository.save(insight);

        return new InsightResponseDto.InsightIdRes(savedInsight.getId());
    }

    @Transactional
    public InsightResponseDto.InsightIdRes updateInsight(Long insightId, InsightRequestDto.UpdateInsight request) {
        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        insight.updateIfChanged(request);

        return new InsightResponseDto.InsightIdRes(insight.getId());
    }

    @Transactional(readOnly = true)
    public InsightResponseDto.SessionInsightListPageRes getInsightsBySession(Long sessionId, String sort, Pageable pageable, String userName) {
        // 정렬 조건 처리

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
    public void deleteInsight(Long insightId) {
        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));
        insightRepository.delete(insight);
    }

    @Transactional(readOnly = true)
    public InsightResponseDto.InsightRes getInsightById(Long insightId) {
        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));
        return InsightMapper.toResponse(insight);
    }

    @Transactional
    public int toggleLike(String username, Long insightId) {

        User user = userService.findByUsername(username);

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
    public List<InsightResponseDto.InsightTopRes> getTopPopularInsights(String username) {
        User user = userService.findByUsername(username);
        List<InsightResponseDto.InsightTopListQueryDto> topList = insightRepository.findTopInsights(user.getId());
        return InsightMapper.toTopInsightList(topList);
    }


    @Transactional(readOnly = true)
    public InsightResponseDto.InsightListPageRes getInsightsByEventDay(
            LocalDate eventDay,
            Long sessionId,
            String sort,
            int page,
            String username
    ) {
        int pageSize = 3;  // 한 페이지당 9개
        Pageable pageable = PageRequest.of(page, pageSize);

        User user = userService.findByUsername(username);
        Page<InsightResponseDto.InsightListQueryDto> insightPage =
                insightRepository.findInsightsByEventDay(eventDay, sessionId, sort, pageable, user.getId());

        if (insightPage.isEmpty()) {
            throw new CustomException(ErrorCode.INSIGHT_NOT_FOUND);
        }

        return InsightMapper.toListPageResponse(insightPage, page, pageSize);
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
