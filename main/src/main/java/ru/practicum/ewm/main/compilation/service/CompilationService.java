package ru.practicum.ewm.main.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.main.compilation.dto.CompilationListDto;
import ru.practicum.ewm.main.compilation.dto.CompilationResponseDto;
import ru.practicum.ewm.main.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.main.compilation.dto.UpdateCompilationRequestDto;

public interface CompilationService {
    CompilationResponseDto addCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationResponseDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilation);

    CompilationResponseDto getCompilation(Long compId);

    CompilationListDto getCompilations(Boolean pinned, Pageable pageable);
}
