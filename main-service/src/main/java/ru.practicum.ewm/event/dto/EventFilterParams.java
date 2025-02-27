package ru.practicum.ewm.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFilterParams {
    // Общие параметры
    List<Long> categories;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    Integer from;
    Integer size;

    // Специфичные для администратора
    List<Long> users;
    List<String> states;

    // Специфичные для публичного запроса
    String text;
    Boolean paid;
    Boolean onlyAvailable;
    String sort;
}