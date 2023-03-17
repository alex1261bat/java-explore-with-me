package ru.practicum.ewm.main.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto category) {
        log.info("Создать категорию:{}", category);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(category));
    }

    @PatchMapping("{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable @Min(1) Long categoryId,
                                                      @RequestBody @Valid CategoryDto updateCategory) {
        log.info("Обновление категории:{} с id={}",updateCategory, categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(updateCategory, categoryId));
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @Min(1) Long categoryId) {
        log.info("Удалить категорию с id={}", categoryId);
        categoryService.deleteCategory(categoryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
