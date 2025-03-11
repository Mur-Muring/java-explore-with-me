package ru.practicum.service;

import ru.practicum.ewm.comments.CommentRepository;
import ru.practicum.ewm.event.model.Event;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.*;
import ru.practicum.ewm.event.EventRepository;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticsServiceBase implements StatisticsService {
     final StatRepository statRepository;
    final EventRepository eventRepository;
    final CommentRepository commentRepository;

    @Override
    @Transactional
    public StatDto create(StatDto statDto) {
        Statistics statistics = StatisticsMapper.toStatistics(statDto);

        return StatisticsMapper.toStatDto(statRepository.save(statistics));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatOutDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (start.isAfter(end)) {
            throw new InvalidParameterException("Дата начала start: " + start + "и дата окончания end: " + end + "не могут быть равны или противоречить друг другу");
        }
        if (unique) {
            if (uris != null) {
                return statRepository.findAllWithUniqueIpWithUris(uris, start, end);
            }
            return statRepository.findAllWithUniqueIpWithoutUris(start, end);
        } else {
            if (uris != null) {
                return statRepository.findAllWithUris(uris, start, end);
            }
            return statRepository.findAllWithoutUris(start, end);
        }
    }

    public List<EventCommentStatDto> getCommentsStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        // Получить события в заданном промежутке времени
        List<Event> events = eventRepository.findByEventDateBetween(start, end); // Вызываем метод, который будет искать события в указанном промежутке времени

        List<EventCommentStatDto> result = new ArrayList<>();

        for (Event event : events) {
            // Подсчитать количество комментариев для каждого события
            long commentCount = commentRepository.countByEventAndCreatedBetween(event, start, end);

            // Добавить статистику в результат
            EventCommentStatDto stat = new EventCommentStatDto(event.getId(), event.getTitle(), commentCount);
            result.add(stat);
        }

        return result;
    }
}