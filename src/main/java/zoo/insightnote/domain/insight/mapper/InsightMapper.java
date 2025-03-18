package zoo.insightnote.domain.insight.mapper;

import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.user.entity.User;

public class InsightMapper {

    public static Insight toEntity(InsightRequestDto.CreateDto request, Session session, User user) {
        return Insight.create(
                session,
                user,
                request.getMemo(),
                request.getIsPublic(),
                request.getIsAnonymous(),
                request.getIsDraft()
        );
    }

    public static InsightResponseDto.InsightRes toResponse(Insight insight) {
        return InsightResponseDto.InsightRes.builder()
                .id(insight.getId())
                .sessionId(insight.getSession().getId())
                .memo(insight.getMemo())
                .isPublic(insight.getIsPublic())
                .isAnonymous(insight.getIsAnonymous())
                .isDraft(insight.getIsDraft())
                .createdAt(insight.getCreateAt())
                .updatedAt(insight.getUpdatedAt())
                .build();
    }
}
