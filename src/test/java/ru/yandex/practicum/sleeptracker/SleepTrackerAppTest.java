package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {

    @Test
    void testSessionCount() {
        var sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL)
        );
        assertEquals("2", new SessionCountFunction().apply(sessions).getValue());
    }

    @Test
    void testSessionCountEmpty() {
        assertEquals("0", new SessionCountFunction().apply(List.of()).getValue());
    }

    @Test
    void testMinDuration() {
        var sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 14, 0),
                        LocalDateTime.of(2025, 10, 3, 14, 30), SleepQuality.NORMAL)
        );
        assertEquals("30", new MinDurationFunction().apply(sessions).getValue());
    }

    @Test
    void testMaxDuration() {
        var sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 14, 0),
                        LocalDateTime.of(2025, 10, 3, 14, 30), SleepQuality.NORMAL)
        );
        assertEquals("480", new MaxDurationFunction().apply(sessions).getValue());
    }

    @Test
    void testAvgDuration() {
        var sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0), SleepQuality.NORMAL)
        );
        assertEquals("480,0", new AvgDurationFunction().apply(sessions).getValue());
    }

    @Test
    void testBadQualityCount() {
        var sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 7, 0), SleepQuality.BAD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 14, 0),
                        LocalDateTime.of(2025, 10, 3, 15, 0), SleepQuality.BAD)
        );
        assertEquals("2", new BadQualityCountFunction().apply(sessions).getValue());
    }




    @Test
    void testSleeplessNightsAllSlept() {
        var s = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 13, 0),
                        LocalDateTime.of(2025, 10, 2, 14, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 22, 0),
                        LocalDateTime.of(2025, 10, 4, 6, 0), SleepQuality.GOOD)
        );
        assertEquals("0", new SleeplessNightsFunction().apply(s).getValue());
    }

    @Test
    void testSleeplessNightsOneMissed() {
        var s = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 22, 0),
                        LocalDateTime.of(2025, 10, 4, 6, 0), SleepQuality.GOOD)
        );
        assertEquals("1", new SleeplessNightsFunction().apply(s).getValue());
    }
    
    @Test
    void testSleeplessNightsFirstAfter12() {
        var s = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 14, 0),
                        LocalDateTime.of(2025, 10, 1, 15, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0), SleepQuality.GOOD)
        );
        assertEquals("0", new SleeplessNightsFunction().apply(s).getValue());
    }

    @Test
    void testSleeplessNightsCrossMonth() {
        var s = List.of(
                new SleepingSession(LocalDateTime.of(2025, 9, 30, 23, 0),
                        LocalDateTime.of(2025, 10, 1, 5, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 7, 0), SleepQuality.GOOD)
        );
        assertEquals("1", new SleeplessNightsFunction().apply(s).getValue());
    }




    @Test
    void testChronotypeOwl() {
        var s = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 10, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 15),
                        LocalDateTime.of(2025, 10, 2, 9, 30), SleepQuality.GOOD)
        );
        assertEquals("OWL", new ChronotypeFunction().apply(s).getValue());
    }

    @Test
    void testChronotypeTieDefaultsToDove() {
        var s = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 10, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 21, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 30), SleepQuality.GOOD)
        );
        assertEquals("DOVE", new ChronotypeFunction().apply(s).getValue());
    }
}