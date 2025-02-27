package ru.practicum.ewm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFilterParams;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events")
public class EventPublicController {
    private final EventService eventService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping
    public List<EventShortDto> getAllEvents(
            @RequestParam(defaultValue = "") final String text,
            @RequestParam(required = false) final List<Long> categories,
            @RequestParam(required = false) final Boolean paid,
            @RequestParam(required = false) final String rangeStart,
            @RequestParam(required = false) final String rangeEnd,
            @RequestParam(defaultValue = "false") final boolean onlyAvailable,
            @RequestParam(required = false) final String sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
            @RequestParam(defaultValue = "10") @Positive final int size,
            final HttpServletRequest request) {

        log.info("==> Запрос (Public) на получение событий с фильтром");

        LocalDateTime start = (rangeStart != null) ? LocalDateTime.parse(rangeStart, formatter) : LocalDateTime.now();
        LocalDateTime end = (rangeEnd != null) ? LocalDateTime.parse(rangeEnd, formatter) : LocalDateTime.MAX;

        EventFilterParams eventFilterParams = EventFilterParams.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(start)
                .rangeEnd(end)
                .onlyAvailable(onlyAvailable)
                .from(from)
                .size(size)
                .sort(Objects.nonNull(sort) ? EventSort.fromString(sort).name() : null)
                .build();

        return eventService.getAllPublic(eventFilterParams, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable(value = "eventId") @Min(1) Long eventId,
                                     HttpServletRequest request) {
        log.info("GET запрос на получение полной информации о событии с id= {}", eventId);
        return eventService.getById(eventId, request);
    }
}