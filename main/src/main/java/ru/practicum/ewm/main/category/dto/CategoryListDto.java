package ru.practicum.ewm.main.category.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryListDto {
    @JsonValue
    private List<CategoryDto> catList;
}
