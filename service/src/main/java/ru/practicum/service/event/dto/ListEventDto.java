package ru.practicum.service.event.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ListEventDto {
    @JsonValue
    private List<EventDto> events;
}
