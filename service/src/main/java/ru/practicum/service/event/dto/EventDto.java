package ru.practicum.service.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.category.dto.CategoryDto;
import ru.practicum.service.event.model.State;
import ru.practicum.service.event.model.Location;
import ru.practicum.service.user.dto.UserDto;

@Data
@Builder
public class EventDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Location location;
    private UserDto initiator;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}
