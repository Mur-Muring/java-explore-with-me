package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.event.model.EventAdminState;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest extends UpdateEventBase {
    private EventAdminState stateAction;
}