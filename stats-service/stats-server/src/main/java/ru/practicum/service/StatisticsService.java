package ru.practicum.service;

import ru.practicum.EventCommentStatDto;
import ru.practicum.StatDto;
import ru.practicum.StatOutDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {

    StatDto create(StatDto statDto);

    List<EventCommentStatDto> getCommentsStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    List<StatOutDto> get(LocalDateTime start,
                         LocalDateTime end,
                         List<String> uris,
                         Boolean unique);
}