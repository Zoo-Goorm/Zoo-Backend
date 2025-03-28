package zoo.insightnote.domain.session.mapper;

import com.querydsl.core.Tuple;
import zoo.insightnote.domain.session.dto.response.SessionDetaileWithImageAndCountResponse;
import zoo.insightnote.domain.session.dto.response.SessionTimeWithAllListGenericResponse;

import java.time.LocalDateTime;
import java.util.List;

public class SessionListWithImageMapper {

    public static SessionTimeWithAllListGenericResponse<SessionDetaileWithImageAndCountResponse> toResponse(List<Tuple> results) {
        return SessionTimeRangeMapper.process(results, tuple -> new SessionDetaileWithImageAndCountResponse(
                tuple.get(0, Long.class), // id
                tuple.get(1, String.class), // name
                tuple.get(3, String.class), // shortDescription
                tuple.get(4, String.class), // location
                tuple.get(5, LocalDateTime.class), // startTime
                tuple.get(6, LocalDateTime.class), // endTime
                SessionTimeRangeMapper.formatTimeRange(tuple.get(5, LocalDateTime.class), tuple.get(6, LocalDateTime.class)), // timeRange
                tuple.get(7, Integer.class), // participantCount
                tuple.get(8, Integer.class), // maxCapacity
                SessionTimeRangeMapper.splitToSet(tuple.get(2, String.class)), // keywords
                tuple.get(10, String.class), // speakerImageUrl
                tuple.get(9, String.class) // speakerName
        ));
    }
}
