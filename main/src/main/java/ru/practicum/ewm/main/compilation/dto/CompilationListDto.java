package ru.practicum.ewm.main.compilation.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class CompilationListDto {
    @JsonValue
    private List<CompilationResponseDto> compilations;
}
