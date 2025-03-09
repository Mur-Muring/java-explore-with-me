package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.event.model.EventUserState;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest extends UpdateEventBase {
    private EventUserState stateAction;
}