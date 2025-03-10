package zoo.insightnote.domain.insight.mapper;

import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.session.entity.Session;

public class InsightMapper {

    public static Insight toEntity(InsightRequestDto.CreateDto request, Session session) {
        return Insight.create(session, request.getMemo(), request.getIsPublic());
    }

    public static InsightResponseDto.InsightRes toResponse(Insight insight) {
        return InsightResponseDto.InsightRes.builder()
                .id(insight.getId())
                .sessionId(insight.getSession().getId())
                .memo(insight.getMemo())
                .isPublic(insight.getIsPublic())
                .createdAt(insight.getCreateAt())
                .updatedAt(insight.getUpdatedAt())
                .build();
    }
}
