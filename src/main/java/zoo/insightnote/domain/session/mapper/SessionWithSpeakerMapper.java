package zoo.insightnote.domain.session.mapper;

import zoo.insightnote.domain.session.dto.response.query.SessionSpeakerDetailQuery;
import zoo.insightnote.domain.session.dto.response.SessionWithSpeakerDetailResponse;

import java.util.*;

public class SessionWithSpeakerMapper {

    public static SessionWithSpeakerDetailResponse toResponse(SessionSpeakerDetailQuery result) {
        List<String> keywords = Optional.ofNullable(result.getKeywords())
                .map(k -> Arrays.stream(k.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .distinct()
                        .toList())
                .orElse(Collections.emptyList());

        List<String> careers = Optional.ofNullable(result.getCareers())
                .map(c -> c.replace(", ", "##SEP##"))
                .map(c -> Arrays.stream(c.split(","))
                        .map(s -> s.replace("##SEP##", ", "))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .distinct()
                        .toList())
                .orElseGet(ArrayList::new);

        return SessionWithSpeakerDetailResponse.builder()
                .sessionName(result.getSessionName())
                .longDescription(result.getLongDescription())
                .location(result.getLocation())
                .maxCapacity(result.getMaxCapacity())
                .participantCount(result.getParticipantCount())
                .speakerName(result.getSpeakerName())
                .keywords(keywords)
                .careers(careers)
                .imageUrl(result.getImageUrl())
                .build();
    }
}