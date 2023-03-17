package ru.practicum.ewm.main.event.dto;

import lombok.*;
import ru.practicum.ewm.main.event.model.Location;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequestDto {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    private String title;
}
