package ru.practicum.ewm.event.controller;

public enum EventSort {
    DATE,
    VIEWS;

    public static EventSort fromString(String value) {
        try {
            return EventSort.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Некорректный параметр сортировки: " + value);
        }
    }
}