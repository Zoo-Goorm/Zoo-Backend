package zoo.insightnote.domain.session.mapper;

import com.querydsl.core.Tuple;
import zoo.insightnote.domain.session.dto.response.SessionTimeWithAllListGenericResponse;
import zoo.insightnote.domain.session.dto.response.SessionTimeWithListGenericResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SessionTimeRangeMapper {
    public static <T> SessionTimeWithAllListGenericResponse<T> process(
            List<Tuple> results,
            Function<Tuple, T> mapFunction
    ) {
        Map<String, Map<String, List<T>>> tempGrouped = new LinkedHashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M월 d일");

        for (Tuple row : results) {
            LocalDateTime startTime = row.get(5, LocalDateTime.class);
            LocalDateTime endTime = row.get(6, LocalDateTime.class);
            String formattedDate = startTime.format(dateFormatter);
            String timeRange = formatTimeRange(startTime, endTime);

            T sessionDto = mapFunction.apply(row);

            tempGrouped
                    .computeIfAbsent(formattedDate, k -> new LinkedHashMap<>())
                    .computeIfAbsent(timeRange, k -> new ArrayList<>())
                    .add(sessionDto);
        }

        Map<String, List<SessionTimeWithListGenericResponse<T>>> finalGrouped = new LinkedHashMap<>();

        for (Map.Entry<String, Map<String, List<T>>> dateEntry : tempGrouped.entrySet()) {
            List<SessionTimeWithListGenericResponse<T>> timeList = new ArrayList<>();

            for (Map.Entry<String, List<T>> timeEntry : dateEntry.getValue().entrySet()) {
                timeList.add(new SessionTimeWithListGenericResponse<>(
                        timeEntry.getKey(),
                        timeEntry.getValue()
                ));
            }

            finalGrouped.put(dateEntry.getKey(), timeList);
        }

        return new SessionTimeWithAllListGenericResponse<>(finalGrouped);
    }

    public static String formatTimeRange(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return start.format(formatter) + "~" + end.format(formatter);
    }

    public static Set<String> splitToSet(String keywordString) {
        if (keywordString == null || keywordString.isBlank()) return Set.of();
        return Arrays.stream(keywordString.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}
