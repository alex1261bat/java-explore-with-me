package ru.practicum.ewm.main.category.dto;

import lombok.*;
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
