package zoo.insightnote.domain.session.mapper;

import com.querydsl.core.Tuple;
import zoo.insightnote.domain.session.dto.response.query.SessionDetailResponse;
import zoo.insightnote.domain.session.dto.response.query.SessionTimeWithAllListGenericResponse;

import java.time.LocalDateTime;
import java.util.*;

import static zoo.insightnote.domain.session.mapper.SessionTimeRangeMapper.formatTimeRange;
import static zoo.insightnote.domain.session.mapper.SessionTimeRangeMapper.splitToSet;

public class SessionListMapper {
    public static SessionTimeWithAllListGenericResponse<SessionDetailResponse> toResponse(List<Tuple> results) {
        return SessionTimeRangeMapper.process(results, tuple -> SessionDetailResponse.builder()
                .id(tuple.get(0, Long.class))
                .name(tuple.get(1, String.class))
                .keywords(splitToSet(tuple.get(2, String.class)))
                .shortDescription(tuple.get(3, String.class))
                .location(tuple.get(4, String.class))
                .startTime(tuple.get(5, LocalDateTime.class))
                .endTime(tuple.get(6, LocalDateTime.class))
                .timeRange(formatTimeRange(tuple.get(5, LocalDateTime.class), tuple.get(6, LocalDateTime.class)))
                .build());
    }
}
