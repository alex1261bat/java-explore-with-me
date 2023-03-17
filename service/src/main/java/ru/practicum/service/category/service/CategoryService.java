package ru.practicum.service.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.service.category.dto.CategoryDto;
import ru.practicum.service.category.dto.CategoryListDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    void deleteCategory(Long catId);

    CategoryListDto getCategories(Pageable pageable);

    CategoryDto getCategoryById(Long catId);
}
