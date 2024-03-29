package ru.practicum.service.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.compilation.dto.CompilationResponseDto;
import ru.practicum.service.compilation.service.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping("{compId}")
    public ResponseEntity<CompilationResponseDto> getCompilation(@PathVariable @Min(1) Long compId) {
        log.info("Получение компиляции с id={}", compId);

        return ResponseEntity.status(HttpStatus.OK).body(compilationService.getCompilation(compId));
    }

    @GetMapping
    public ResponseEntity<List<CompilationResponseDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                @RequestParam(defaultValue = "0") @Min(0) Integer from) {
        log.info("Получение списка компиляций");

        return ResponseEntity.status(HttpStatus.OK)
                .body(compilationService.getCompilations(pinned, PageRequest.of(from / size, size)));
    }
}
