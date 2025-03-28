package zoo.insightnote.domain.session.mapper;

import com.querydsl.core.Tuple;
import zoo.insightnote.domain.session.dto.response.SessionDetailResponse;
import zoo.insightnote.domain.session.dto.response.SessionTimeWithAllListResponse;
import zoo.insightnote.domain.session.dto.response.SessionTimeWithListResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class SessionSplitTimeRangeListMapper {
    public static SessionTimeWithAllListResponse processResultsForSessionAllRes(List<Tuple> results) {
        Map<String, Map<String, List<SessionDetailResponse>>> tempGrouped = new LinkedHashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M월 d일");

        for (Tuple row : results) {
            LocalDateTime startTime = row.get(5, LocalDateTime.class);
            LocalDateTime endTime = row.get(6, LocalDateTime.class);
            String formattedDate = startTime.format(dateFormatter);
            String timeRange = formatTimeRange(startTime, endTime);

            SessionDetailResponse detail = mapToSessionDetail(row, timeRange);

            tempGrouped
                    .computeIfAbsent(formattedDate, k -> new LinkedHashMap<>())
                    .computeIfAbsent(timeRange, k -> new ArrayList<>())
                    .add(detail);
        }

        Map<String, List<SessionTimeWithListResponse>> finalGrouped = new LinkedHashMap<>();

        for (Map.Entry<String, Map<String, List<SessionDetailResponse>>> dateEntry : tempGrouped.entrySet()) {
            List<SessionTimeWithListResponse> timeList = new ArrayList<>();

            for (Map.Entry<String, List<SessionDetailResponse>> timeEntry : dateEntry.getValue().entrySet()) {
                SessionTimeWithListResponse sessionTimeGroup = SessionTimeWithListResponse.builder()
                        .timeRange(timeEntry.getKey())
                        .sessions(timeEntry.getValue())
                        .build();

                timeList.add(sessionTimeGroup); // 리스트에 추가해줘야 함
            }

            finalGrouped.put(dateEntry.getKey(), timeList);
        }

        return SessionTimeWithAllListResponse.builder()
                .sessionsByDay(finalGrouped)
                .build();
    }

    private static SessionDetailResponse mapToSessionDetail(Tuple row, String timeRange) {
        return SessionDetailResponse.builder()
                .id(row.get(0, Long.class))
                .name(row.get(1, String.class))
                .keywords(splitToSet(row.get(2, String.class)))
                .shortDescription(row.get(3, String.class))
                .location(row.get(4, String.class))
                .startTime(row.get(5, LocalDateTime.class))
                .endTime(row.get(6, LocalDateTime.class))
                .timeRange(timeRange)
                .build();
    }

    private static String formatTimeRange(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return start.format(formatter) + "~" + end.format(formatter);
    }

    private static Set<String> splitToSet(String keywordString) {
        if (keywordString == null || keywordString.isBlank()) return Set.of();
        return Arrays.stream(keywordString.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}
