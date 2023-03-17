package ru.practicum.ewm.main.compilation.dto;

import lombok.*;
import ru.practicum.ewm.main.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationResponseDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
