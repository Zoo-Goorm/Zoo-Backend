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
        return new InsightResponseDto.InsightRes(
                insight.getId(),
                insight.getSession().getId(),
                insight.getMemo(),
                insight.getIsPublic(),
                insight.getCreateAt(),
                insight.getUpdatedAt()
        );
    }
}
