package ru.practicum.service.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.category.dto.CategoryDto;
import ru.practicum.service.category.dto.CategoryListDto;
import ru.practicum.service.category.service.CategoryService;

import javax.validation.constraints.Min;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/categories")
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoryListDto> getCategories(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                         @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.info("Получить категории: from: {},size: {}", from, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.getCategories(PageRequest.of(from / size, size)));
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable @Min(1) Long categoryId) {
        log.info("Получить категорию с id={}", categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryById(categoryId));
    }
}
