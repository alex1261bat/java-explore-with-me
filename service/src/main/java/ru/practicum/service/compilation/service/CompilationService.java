package ru.practicum.service.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.service.compilation.dto.CompilationListDto;
import ru.practicum.service.compilation.dto.CompilationResponseDto;
import ru.practicum.service.compilation.dto.NewCompilationDto;
import ru.practicum.service.compilation.dto.UpdateCompilationRequestDto;

public interface CompilationService {
    CompilationResponseDto addCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationResponseDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilation);

    CompilationResponseDto getCompilation(Long compId);

    CompilationListDto getCompilations(Boolean pinned, Pageable pageable);
}
