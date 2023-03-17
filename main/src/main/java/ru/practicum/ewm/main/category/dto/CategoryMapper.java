package ru.practicum.ewm.main.category.dto;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import ru.practicum.ewm.main.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category mapToCategory(CategoryDto categoryDto);

    CategoryDto mapToCategoryDto(Category category);

    List<CategoryDto> mapToListCategories(Page<Category> page);
}
