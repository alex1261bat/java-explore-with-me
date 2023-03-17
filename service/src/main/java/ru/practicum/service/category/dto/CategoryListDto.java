package ru.practicum.service.category.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryListDto {
    @JsonValue
    private List<CategoryDto> catList;
}
