package zoo.insightnote.domain.insight.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.InsightLike.entity.InsightLike;
import zoo.insightnote.domain.InsightLike.repository.InsightLikeRepository;
import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.service.ImageService;
import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.mapper.InsightMapper;
import zoo.insightnote.domain.insight.repository.InsightQueryRepository;
import zoo.insightnote.domain.insight.repository.InsightRepository;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.sessionKeyword.repository.SessionKeywordRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.domain.userIntroductionLink.entity.UserIntroductionLink;
import zoo.insightnote.domain.userIntroductionLink.repository.UserIntroductionLinkRepository;
import zoo.insightnote.domain.voteOption.entity.VoteOption;
import zoo.insightnote.domain.voteOption.repository.VoteOptionRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsightService {

    private final InsightRepository insightRepository;
    private final InsightLikeRepository insightLikeRepository;
    private final SessionRepository sessionRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final UserIntroductionLinkRepository userIntroductionLinkRepository;
    private final SessionKeywordRepository sessionKeywordRepository;

    @Transactional
    public InsightResponseDto.InsightRes saveOrUpdateInsight(InsightRequestDto.CreateDto request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        Optional<Insight> existingInsight = insightRepository.findBySessionAndUser(session, user);
        Insight savedInsight;

        if (existingInsight.isPresent()) {
            // 기존에 저장된 인사이트가 있으면 업데이트
            Insight insight = existingInsight.get();
            insight.updateIfChanged(request.getMemo(), request.getIsPublic(), request.getIsAnonymous(), request.getIsDraft(), request.getVoteTitle());

            // **기존 투표 제목과 비교**
            boolean isVoteTitleChanged = !Objects.equals(insight.getVoteTitle(), request.getVoteTitle());

            // **기존 투표 옵션과 비교**
            List<String> existingOptions = voteOptionRepository.findByInsight(insight).stream()
                    .map(VoteOption::getOptionText)
                    .toList();

            List<String> newOptions = request.getVoteOptions();

            boolean isVoteOptionsChanged = !existingOptions.equals(newOptions);

            // **기존 제목 또는 옵션이 변경되었을 경우에만 업데이트**
            if (isVoteTitleChanged || isVoteOptionsChanged) {
                voteOptionRepository.deleteByInsight(insight); // 기존 투표 삭제
                saveVoteOptions(insight, request.getVoteOptions()); // 새 투표 저장
            }

            // 정식 저장이면 finalizeDraft() 호출
            if (!request.getIsDraft()) {
                insight.finalizeDraft();
            }

            savedInsight = insight;
            imageService.updateImages(new ImageRequest.UploadImages(savedInsight.getId(), EntityType.INSIGHT, request.getImages()));
        } else {
            // 기존 데이터가 없으면 새롭게 생성
            Insight insight = InsightMapper.toEntity(request, session, user);
            savedInsight = insightRepository.save(insight);

            if (request.getVoteTitle() != null && request.getVoteOptions() != null) {
                saveVoteOptions(savedInsight, request.getVoteOptions());
            }


            // 이미지 값이 있으면 저장을 하는 로직이 필요하다
            imageService.saveImages(savedInsight.getId(), EntityType.INSIGHT, request.getImages());
        }

        return InsightMapper.toResponse(savedInsight);
    }

    @Transactional
    public void saveVoteOptions(Insight insight, List<String> voteOptionTexts) {
        List<VoteOption> voteOptions = voteOptionTexts.stream()
                .map(optionText -> new VoteOption(insight, optionText))
                .collect(Collectors.toList());

        voteOptionRepository.saveAll(voteOptions);
    }

    @Transactional
    public InsightResponseDto.InsightRes updateInsight(Long insightId, InsightRequestDto.UpdateDto request) {
        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        insight.updateIfChanged(request.getMemo(), request.getIsPublic(), request.getIsAnonymous(),request.getIsDraft(), request.getVoteTitle());

        imageService.updateImages(new ImageRequest.UploadImages(insight.getId(), EntityType.INSIGHT, request.getImages()));

        return InsightMapper.toResponse(insight);
    }

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
    public int toggleLike(Long userId, Long insightId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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
    public List<InsightResponseDto.InsightTopRes> getTopPopularInsights() {
        return insightRepository.findTopInsights();
    }

    // 인사이트 목록 9개 기준 (시간순 정렬)
    // 무한 스크롤 (페이징)
//    @Transactional(readOnly = true)
//    public InsightResponseDto.InsightListPageRes getInsightsByEventDay(LocalDate eventDay, int page) {
//        int pageSize = 9;  // 한 페이지당 9개씩 가져옴
//        Pageable pageable = PageRequest.of(page, pageSize);
//
//        Page<InsightResponseDto.InsightListQueryDto> insightPage = insightRepository.findInsightsByEventDay(eventDay, pageable);
//
//        if (insightPage.isEmpty()) {
//            throw new CustomException(ErrorCode.INSIGHT_NOT_FOUND);
//        }
//
//        return InsightResponseDto.InsightListPageRes.builder()
//                .hasNext(insightPage.hasNext())
//                .totalElements(insightPage.getTotalElements())
//                .totalPages(insightPage.getTotalPages())
//                .content(InsightMapper.toListPageResponse(insightPage.getContent()))  // 매퍼 활용
//                .pageNumber(page)
//                .pageSize(pageSize)
//                .build();
//    }

    @Transactional(readOnly = true)
    public InsightResponseDto.InsightListPageRes getInsightsByEventDay(
            LocalDate eventDay,
            Long sessionId,
            String sort,
            int page
    ) {
        int pageSize = 9;  // 한 페이지당 9개
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<InsightResponseDto.InsightListQueryDto> insightPage =
                insightRepository.findInsightsByEventDay(eventDay, sessionId, sort, pageable);

        if (insightPage.isEmpty()) {
            throw new CustomException(ErrorCode.INSIGHT_NOT_FOUND);
        }

        return InsightMapper.toListPageResponse(insightPage, page, pageSize);
//        return InsightResponseDto.InsightListPageRes.builder()
//                .hasNext(insightPage.hasNext())
//                .totalElements(insightPage.getTotalElements())
//                .totalPages(insightPage.getTotalPages())
//                .content(InsightMapper.toListPageResponse(insightPage.getContent()))  // 매핑 로직 그대로
//                .pageNumber(page)
//                .pageSize(pageSize)
//                .build();
    }



    // 인사이트 상세 페이지
    @Transactional(readOnly = true)
    public InsightResponseDto.InsightDetailPageRes getInsightDetail(Long insightId) {

        InsightResponseDto.InsightDetailQueryDto insightDto = insightRepository.findByIdWithSessionAndUser(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        return InsightMapper.toDetailPageResponse(insightDto);
    }



}
