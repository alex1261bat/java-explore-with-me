package ru.practicum.service.compilation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateCompilationRequestDto {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
