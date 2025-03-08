package ru.practicum.ewm.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.constants.DateFormatConstants;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentOutDto {
    Long id;
    String text;
    Long authorId;
    Long eventId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormatConstants.DATE_TIME_PATTERN)
    LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormatConstants.DATE_TIME_PATTERN)
    LocalDateTime lastUpdatedOn;
}