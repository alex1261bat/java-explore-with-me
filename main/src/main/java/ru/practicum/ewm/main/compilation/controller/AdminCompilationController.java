package ru.practicum.ewm.main.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.compilation.dto.CompilationResponseDto;
import ru.practicum.ewm.main.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.main.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.ewm.main.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationResponseDto> addCompilation(@RequestBody @Valid NewCompilationDto compilation) {
        log.info("Добавить компиляцию:{}", compilation);

        return ResponseEntity.status(HttpStatus.CREATED).body(compilationService.addCompilation(compilation));
    }

    @DeleteMapping("{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable @Min(1) Long compId) {
        log.info("Удалить компиляцию с id={}", compId);
        compilationService.deleteCompilation(compId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("{compId}")
    public ResponseEntity<CompilationResponseDto> updateCompilation(@PathVariable @Min(1) Long compId,
                                                                @RequestBody UpdateCompilationRequestDto updateCompilation) {
        log.info("Обновить компиляцию с id={} на компиляцию:{}", compId, updateCompilation);

        return ResponseEntity.status(HttpStatus.OK).body(compilationService.updateCompilation(compId, updateCompilation));
    }
}
