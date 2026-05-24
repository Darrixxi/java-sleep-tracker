package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(s -> s.getStart().toLocalTime().isBefore(LocalTime.of(6, 0)) ||
                        s.getStart().toLocalTime().isAfter(LocalTime.of(18, 0)) ||
                        s.getStart().toLocalDate().isBefore(s.getEnd().toLocalDate()))
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
        if (start.isAfter(LocalTime.of(23, 0)) && end.isAfter(LocalTime.of(9, 0)))
            return Chronotype.OWL;
        if (start.isBefore(LocalTime.of(22, 0)) && end.isBefore(LocalTime.of(7, 0)))
            return Chronotype.LARK;
        return Chronotype.DOVE;
    }
}
