package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {

    public static void main(String[] args) throws IOException {

        try (InputStream is = SleepTrackerApp.class.getClassLoader().getResourceAsStream("sleep_log.txt")) {

            if (is == null) {
                System.err.println("Ошибка: Файл sleep_log.txt не найден!");
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

                List<SleepingSession> sessions = reader.lines()
                        .filter(line -> !line.isBlank())
                        .map(line -> {
                            String[] parts = line.split(";");
                            return new SleepingSession(
                                    LocalDateTime.parse(parts[0].trim(), formatter),
                                    LocalDateTime.parse(parts[1].trim(), formatter),
                                    SleepQuality.valueOf(parts[2].trim().toUpperCase())
                            );
                        })
                        .toList();

                List<Function<List<SleepingSession>, SleepAnalysisResult>> analyzers = List.of(
                        new SessionCountFunction(),
                        new MinDurationFunction(),
                        new MaxDurationFunction(),
                        new AvgDurationFunction(),
                        new BadQualityCountFunction(),
                        new SleeplessNightsFunction(),
                        new ChronotypeFunction()
                );

                analyzers.forEach(analyzer -> System.out.println(analyzer.apply(sessions)));

            } catch (IOException e) {
                System.err.println("Ошибка чтения файла: " + e.getMessage());
            } catch (DateTimeParseException e) {
                System.err.println("Ошибка формата данных в строке лога: " + e.getMessage());
            }
        }
    }
}