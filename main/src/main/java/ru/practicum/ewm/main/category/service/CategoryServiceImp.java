package ru.practicum.ewm.main.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.category.dto.CategoryListDto;
import ru.practicum.ewm.main.category.dto.CategoryMapper;
import ru.practicum.ewm.main.category.repository.CategoryRepository;
import ru.practicum.ewm.main.exceptions.NotFoundException;


@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categories;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return categoryMapper.mapToCategoryDto(categories.save(categoryMapper.mapToCategory(categoryDto)));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto updateCategory, Long categoryId) {
        if (categories.existsById(categoryId)) {
            return categoryMapper.mapToCategoryDto(categories.save(categoryMapper.mapToCategory(updateCategory)));
        } else {
            throw new NotFoundException("Категория с id=" + categoryId + " не найдена");
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        if (!categories.existsById(categoryId)) {
            throw new NotFoundException("Категория с id=" + categoryId + " не найдена");
        } else {
            categories.deleteById(categoryId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryListDto getCategories(Pageable pageable) {
        return CategoryListDto.builder()
                .catList(categoryMapper.mapToListCategories(categories.findAll(pageable)))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long categoryId) {
        return categoryMapper.mapToCategoryDto(categories.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + categoryId + " не найдена")));
    }
}
