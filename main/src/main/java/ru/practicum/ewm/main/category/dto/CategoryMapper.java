package ru.practicum.ewm.main.category.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.practicum.ewm.main.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category mapToCategory(CategoryDto categoryDto);

    @Mapping(source = "categoryId", target = "id")
    CategoryDto mapToCategoryDto(Category category);

    List<CategoryDto> mapToListCategories(Page<Category> page);
}
