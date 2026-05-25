package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long minutes = sessions.stream()
                .map(SleepingSession::getDuration)
                .min(Duration::compareTo)
                .map(Duration::toMinutes)
                .orElse(0L);
        return new SleepAnalysisResult("Минимальная продолжительность сессии (мин):", String.valueOf(minutes));
    }
}
