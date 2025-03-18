package zoo.insightnote.domain.insight.service;

import lombok.RequiredArgsConstructor;
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
import zoo.insightnote.domain.insight.repository.InsightRepository;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsightService {


    private final InsightRepository insightRepository;
    private final SessionRepository sessionRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final InsightLikeRepository insightLikeRepository;

    @Transactional
    public InsightResponseDto.InsightRes createInsight(InsightRequestDto.CreateDto request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));


        Optional<Insight> existingInsight = insightRepository.findBySessionAndUser(session, user);
        Insight savedInsight;

        if (existingInsight.isPresent()) {
            Insight insight = existingInsight.get();
            insight.updateIfChanged(request.getMemo(), request.getIsPublic(), request.getIsAnonymous());

            if (insight.getIsDraft()) {
                insight.finalizeDraft();
            }
            savedInsight = insight;
            imageService.updateImages(new ImageRequest.UploadImages(savedInsight.getId(), EntityType.INSIGHT, request.getImages()));
        } else {
            Insight insight = InsightMapper.toEntity(request, session, user);
            savedInsight = insightRepository.save(insight);
            imageService.saveImages(savedInsight.getId(), EntityType.INSIGHT, request.getImages());
        }

        return InsightMapper.toResponse(savedInsight);
    }

    @Transactional
    public InsightResponseDto.InsightRes updateInsight(Long insightId, InsightRequestDto.UpdateDto request) {
        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        insight.updateIfChanged(request.getMemo(), request.getIsPublic(), request.getIsAnonymous());

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


    public int toggleLike(Long userId, Long insightId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        // 이미 좋아요가 있는지 확인
        Optional<InsightLike> existingLike = insightLikeRepository.findByUserAndInsight(user, insight);

        if (existingLike.isPresent()) {
            // 이미 좋아요가 있다면 => 좋아요 취소
            insightLikeRepository.delete(existingLike.get());
            return -1;
        } else {
            InsightLike newLike = InsightLike.builder()
                    .user(user)
                    .insight(insight)
                    .build();

            insightLikeRepository.save(newLike);
            return 1;
        }
    }

}
