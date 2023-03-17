package ru.practicum.ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class ListEventShortDto {
    @JsonValue
    private List<EventShortDto> events;
}
