package zoo.insightnote.domain.session.mapper;

import zoo.insightnote.domain.session.dto.response.query.SessionSpeakerDetailQuery;
import zoo.insightnote.domain.session.dto.response.SessionWithSpeakerDetailResponse;

import java.util.*;

public class SessionWithSpeakerMapper {

    public static SessionWithSpeakerDetailResponse toResponse(SessionSpeakerDetailQuery result) {
        List<String> keywords = Optional.ofNullable(result.keywords())
                .map(k -> Arrays.stream(k.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .distinct()
                        .toList())
                .orElse(Collections.emptyList());

        List<String> careers = Optional.ofNullable(result.careers())
                .map(c -> c.replace(", ", "##SEP##"))
                .map(c -> Arrays.stream(c.split(","))
                        .map(s -> s.replace("##SEP##", ", "))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .distinct()
                        .toList())
                .orElseGet(ArrayList::new);

        return new SessionWithSpeakerDetailResponse(
                result.sessionName(),
                result.longDescription(),
                result.location(),
                result.maxCapacity(),
                result.participantCount(),
                result.speakerName(),
                keywords,
                careers,
                result.imageUrl()
        );
    }
}