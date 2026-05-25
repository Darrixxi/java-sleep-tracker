package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MaxDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long minutes = sessions.stream()
                .map(SleepingSession::getDuration)
                .max(Duration::compareTo)
                .map(Duration::toMinutes)
                .orElse(0L);
        return new SleepAnalysisResult("Максимальная продолжительность сессии (мин):", String.valueOf(minutes));
    }
}

