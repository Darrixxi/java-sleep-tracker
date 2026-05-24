package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Бессонные ночи:", "0");
        }

        LocalDateTime minStart = sessions.stream()
                .map(SleepingSession::getStart)
                .min(Comparator.naturalOrder())
                .orElseThrow();

        LocalDateTime maxEnd = sessions.stream()
                .map(SleepingSession::getEnd)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        LocalDate firstNight = minStart.getHour() >= 12
                ? minStart.toLocalDate().plusDays(1)
                : minStart.toLocalDate().minusDays(1);
        LocalDate lastNight = maxEnd.toLocalDate();

        long nightsCount = lastNight.toEpochDay() - firstNight.toEpochDay() + 1;

        long sleeplessCount = Stream.iterate(firstNight, date -> date.plusDays(1))
                .limit(nightsCount)
                .filter(date -> sessions.stream()
                        .noneMatch(s -> s.getStart().isBefore(date.atTime(6, 0))
                                && s.getEnd().isAfter(date.atStartOfDay())))
                .count();

        return new SleepAnalysisResult("Бессонные ночи:", String.valueOf(sleeplessCount));
    }
}
