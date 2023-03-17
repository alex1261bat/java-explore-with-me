package ru.practicum.service.compilation.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CompilationListDto {
    @JsonValue
    private List<CompilationResponseDto> compilations;
}
