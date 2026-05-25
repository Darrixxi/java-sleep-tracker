package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final LocalTime DAY_FILTER_START = LocalTime.of(6, 0);
    private static final LocalTime DAY_FILTER_END = LocalTime.of(18, 0);

    private static final LocalTime OWL_SLEEP_AFTER = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKE_AFTER = LocalTime.of(9, 0);

    private static final LocalTime LARK_SLEEP_BEFORE = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKE_BEFORE = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(this::isNightSession)
                .collect(Collectors.groupingBy(this::classify, Collectors.counting()));

        if (counts.isEmpty()) return new SleepAnalysisResult("Хронотип пользователя:", "Не определён");

        long maxVal = Collections.max(counts.values());
        List<Chronotype> leaders = counts.entrySet().stream()
                .filter(e -> e.getValue() == maxVal)
                .map(Map.Entry::getKey)
                .toList();

        Chronotype result = leaders.size() > 1 ? Chronotype.DOVE : leaders.get(0);
        return new SleepAnalysisResult("Хронотип пользователя:", result.name());
    }

    private Chronotype classify(SleepingSession s) {
        LocalTime start = s.getStart().toLocalTime();
        LocalTime end = s.getEnd().toLocalTime();
        if (start.isAfter(OWL_SLEEP_AFTER) && end.isAfter(OWL_WAKE_AFTER))
            return Chronotype.OWL;
        if (start.isBefore(LARK_SLEEP_BEFORE) && end.isBefore(LARK_WAKE_BEFORE))
            return Chronotype.LARK;
        return Chronotype.DOVE;
    }

    private boolean isNightSession(SleepingSession session) {
        LocalTime start = session.getStart().toLocalTime();
        boolean crossesMidnight = session.getStart().toLocalDate()
                .isBefore(session.getEnd().toLocalDate());

        return start.isBefore(DAY_FILTER_START) ||
                start.isAfter(DAY_FILTER_END) ||
                crossesMidnight;
    }
}
