package zoo.insightnote.domain.session.mapper;

import com.querydsl.core.Tuple;
import zoo.insightnote.domain.session.dto.response.SessionDetailResponse;
import zoo.insightnote.domain.session.dto.response.SessionTimeWithAllListGenericResponse;

import java.time.LocalDateTime;
import java.util.*;

import static zoo.insightnote.domain.session.mapper.SessionTimeRangeMapper.formatTimeRange;
import static zoo.insightnote.domain.session.mapper.SessionTimeRangeMapper.splitToSet;

public class SessionListMapper {
    public static SessionTimeWithAllListGenericResponse<SessionDetailResponse> toResponse(List<Tuple> results) {
        return SessionTimeRangeMapper.process(results, tuple -> new SessionDetailResponse(
                tuple.get(0, Long.class),
                tuple.get(1, String.class),
                tuple.get(3, String.class),
                tuple.get(4, String.class),
                tuple.get(5, LocalDateTime.class),
                tuple.get(6, LocalDateTime.class),
                formatTimeRange(tuple.get(5, LocalDateTime.class), tuple.get(6, LocalDateTime.class)),
                splitToSet(tuple.get(2, String.class))
        ));
    }
}