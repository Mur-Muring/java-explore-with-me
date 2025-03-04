package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
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
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    private final EventService eventService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // private static final LocalDateTime MAX_TIMESTAMP = LocalDateTime.of(294276, 12, 31, 23, 59, 59); Удалю позже

    @GetMapping
    public List<EventFullDto> getAll(
            @RequestParam(required = false) final List<Long> users,
            @RequestParam(required = false) final List<String> states,
            @RequestParam(required = false) final List<Long> categories,
            @RequestParam(required = false) final String rangeStart,
            @RequestParam(required = false) final String rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
            @RequestParam(defaultValue = "10") @Positive final int size) {

        log.info("==> Запрос на получение всех событий (ADMIN)");

        LocalDateTime start = (rangeStart != null) ? LocalDateTime.parse(rangeStart, formatter) : LocalDateTime.now();
        LocalDateTime end = (rangeEnd != null) ? LocalDateTime.parse(rangeEnd, formatter) : LocalDateTime.now();

        EventFilterParams eventFilterParams = EventFilterParams.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(start)
                .rangeEnd(end)
                .from(from)
                .size(size)
                .build();

        return eventService.getAllAdmin(eventFilterParams);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable(value = "eventId") @Min(1) Long eventId,
                               @RequestBody @Valid UpdateEventAdminRequest inputUpdate) {

        log.info("==> ЗАПОРС на обновление списка событий");
        return eventService.updateAdmin(eventId, inputUpdate);
    }
}