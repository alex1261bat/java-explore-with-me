package ru.practicum.service.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.service.compilation.dto.CompilationResponseDto;
import ru.practicum.service.compilation.dto.NewCompilationDto;
import ru.practicum.service.compilation.dto.UpdateCompilationRequestDto;

import java.util.List;

public interface CompilationService {
    CompilationResponseDto addCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationResponseDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilation);

    CompilationResponseDto getCompilation(Long compId);

    List<CompilationResponseDto> getCompilations(Boolean pinned, Pageable pageable);
}
