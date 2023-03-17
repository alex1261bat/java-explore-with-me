package ru.practicum.service.category.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Data
@Builder
@Jacksonized
public class CategoryDto {
    private Long id;
    @NotNull
    private String name;
}
