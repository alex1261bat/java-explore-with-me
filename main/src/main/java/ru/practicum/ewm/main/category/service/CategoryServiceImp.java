package ru.practicum.ewm.main.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.category.dto.CategoryListDto;
import ru.practicum.ewm.main.category.dto.CategoryMapper;
import ru.practicum.ewm.main.category.repository.CategoryRepository;
import ru.practicum.ewm.main.exceptions.NotFoundException;


@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categories;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return CategoryMapper.INSTANCE.mapToCategoryDto(
                categories.save(CategoryMapper.INSTANCE.mapToCategory(categoryDto)));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto updateCategory, Long categoryId) {
        if (categories.existsByCategoryId(categoryId)) {
            return CategoryMapper.INSTANCE.mapToCategoryDto(
                    categories.save(CategoryMapper.INSTANCE.mapToCategory(updateCategory)));
        } else {
            throw new NotFoundException("Категория с id=" + categoryId + " не найдена");
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!categories.existsByCategoryId(categoryId)) {
            throw new NotFoundException("Категория с id=" + categoryId + " не найдена");
        } else {
            categories.deleteByCategoryId(categoryId);
        }
    }

    @Override
    public CategoryListDto getCategories(Pageable pageable) {
        return CategoryListDto
                .builder()
                .catList(CategoryMapper.INSTANCE.mapToListCategories(categories.findAll(pageable)))
                .build();
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return CategoryMapper.INSTANCE.mapToCategoryDto(categories.findByCategoryId(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + categoryId + " не найдена")));
    }
}
