package ru.practicum.service.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationResponseDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
