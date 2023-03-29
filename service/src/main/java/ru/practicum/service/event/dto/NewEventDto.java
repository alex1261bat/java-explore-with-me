package ru.practicum.service.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.event.model.Location;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class NewEventDto {
    @NotNull
    private String annotation;
    @NotNull
    @Min(1)
    private Long category;
    @NotNull
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    @Min(0)
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull
    private String title;
}
