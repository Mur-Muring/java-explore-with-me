package ru.practicum;

public class StatisticsMapper {

    public static StatDto toStatDto(Statistics statistics) {
        return StatDto.builder()
                .app(statistics.getApp())
                .uri(statistics.getUri())
                .timestamp(statistics.getTimestamp())
                .build();
    }

    public static Statistics toStatistics(StatDto statDto) {
        return new Statistics(
                null,
                statDto.getApp(),
                statDto.getUri(),
                statDto.getIp(),
                statDto.getTimestamp()
        );
    }
}