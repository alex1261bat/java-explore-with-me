package ru.practicum.ewm.main.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.main.category.dto.CategoryListDto;
import ru.practicum.ewm.main.category.dto.CategoryDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    void deleteCategory(Long catId);

    CategoryListDto getCategories(Pageable pageable);

    CategoryDto getCategoryById(Long catId);
}
