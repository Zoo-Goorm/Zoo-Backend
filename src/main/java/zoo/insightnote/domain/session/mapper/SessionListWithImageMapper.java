package zoo.insightnote.domain.session.mapper;

import com.querydsl.core.Tuple;
import zoo.insightnote.domain.session.dto.response.SessionDetaileWithImageAndCountResponse;
import zoo.insightnote.domain.session.dto.response.SessionTimeWithAllListGenericResponse;

import java.time.LocalDateTime;
import java.util.List;

public class SessionListWithImageMapper {

    public static SessionTimeWithAllListGenericResponse<SessionDetaileWithImageAndCountResponse> toResponse(List<Tuple> results) {
        return SessionTimeRangeMapper.process(results, tuple -> SessionDetaileWithImageAndCountResponse.builder()
                .id(tuple.get(0, Long.class))
                .name(tuple.get(1, String.class))
                .keywords(SessionTimeRangeMapper.splitToSet(tuple.get(2, String.class)))
                .shortDescription(tuple.get(3, String.class))
                .location(tuple.get(4, String.class))
                .startTime(tuple.get(5, LocalDateTime.class))
                .endTime(tuple.get(6, LocalDateTime.class))
                .timeRange(SessionTimeRangeMapper.formatTimeRange(tuple.get(5, LocalDateTime.class), tuple.get(6, LocalDateTime.class)))
                .participantCount(tuple.get(7, Integer.class))
                .maxCapacity(tuple.get(8, Integer.class))
                .speakerName(tuple.get(9, String.class))
                .speakerImageUrl(tuple.get(10, String.class))
                .build());
    }
}

