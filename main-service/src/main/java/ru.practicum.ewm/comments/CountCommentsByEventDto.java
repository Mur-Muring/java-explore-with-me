package ru.practicum.ewm.comments;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountCommentsByEventDto {
    Long eventId;
    Long countComments;
}
