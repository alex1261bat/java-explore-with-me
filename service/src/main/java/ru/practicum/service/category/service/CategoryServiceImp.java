package ru.practicum.service.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.category.dto.CategoryDto;
import ru.practicum.service.category.dto.CategoryMapper;
import ru.practicum.service.category.repository.CategoryRepository;
import ru.practicum.service.exceptions.NotFoundException;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
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
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryMapper.mapToListCategories(categories.findAll(pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long categoryId) {
        return categoryMapper.mapToCategoryDto(categories.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + categoryId + " не найдена")));
    }
}
