package ru.practicum.ewm.main.compilation.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
public class UpdateCompilationRequestDto {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
