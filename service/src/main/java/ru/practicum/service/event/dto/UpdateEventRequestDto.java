package ru.practicum.service.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.event.model.Location;

@Data
@Builder
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
