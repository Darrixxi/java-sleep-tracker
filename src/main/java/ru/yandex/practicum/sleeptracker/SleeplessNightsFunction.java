package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final int NOON_HOUR = 12;
    private static final int NIGHT_END_HOUR = 6;

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

        LocalDate firstNight = minStart.getHour() >= NOON_HOUR
                ? minStart.toLocalDate().plusDays(1)
                : minStart.toLocalDate().minusDays(1);
        LocalDate lastNight = maxEnd.toLocalDate();

        long nightsCount = lastNight.toEpochDay() - firstNight.toEpochDay() + 1;

        long sleeplessCount = Stream.iterate(firstNight, date -> date.plusDays(1))
                .limit(nightsCount)
                .filter(date -> sessions.stream()
                        .noneMatch(s -> s.getStart().isBefore(date.atTime(NIGHT_END_HOUR, 0))
                                && s.getEnd().isAfter(date.atStartOfDay())))
                .count();

        return new SleepAnalysisResult("Бессонные ночи:", String.valueOf(sleeplessCount));
    }
}
