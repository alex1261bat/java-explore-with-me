package ru.practicum.ewm.main.compilation.dto;

import lombok.*;
import ru.practicum.ewm.main.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationResponseDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;

}
