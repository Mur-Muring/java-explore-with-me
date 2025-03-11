package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCommentStatDto {
    private Long eventId;
    private String eventTitle;
    private long commentCount;
}
