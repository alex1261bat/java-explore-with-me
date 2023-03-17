package ru.practicum.ewm.main.compilation.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.compilation.dto.*;
import ru.practicum.ewm.main.compilation.model.Compilation;
import ru.practicum.ewm.main.compilation.model.QCompilation;
import ru.practicum.ewm.main.compilation.repository.CompilationRepository;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.exceptions.NotFoundException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompilationServiceImp implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationResponseDto addCompilation(NewCompilationDto compilationDto) {
        Set<Event> findEvents = eventRepository.findAllByEventIdIn(compilationDto.getEvents());
        Compilation compilation = CompilationMapper.INSTANCE.mapToCompilation(compilationDto);
        compilation.setEvents(findEvents);

        return CompilationMapper.INSTANCE.mapToCompilationResp(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        if (compilationRepository.existsByCompilationId(compilationId)) {
            compilationRepository.deleteByCompilationId(compilationId);
        } else {
            throw new NotFoundException("Компиляция с id=" + compilationId + " не найдена");
        }
    }

    @Override
    public CompilationResponseDto updateCompilation(Long compilationId, UpdateCompilationRequestDto updateCompilation) {
        Compilation compilation = compilationRepository.findByCompilationId(compilationId)
                .orElseThrow(() -> new NotFoundException("Компиляция с id=" + compilationId + " не найдена"));
        Set<Event> findEvents = eventRepository.findAllByEventIdIn(updateCompilation.getEvents());
        compilation = CompilationMapper.INSTANCE.mapToCompilation(updateCompilation, compilation);
        compilation.setEvents(findEvents);

        return CompilationMapper.INSTANCE.mapToCompilationResp(compilationRepository.save(compilation));
    }

    @Override
    public CompilationResponseDto getCompilation(Long compilationId) {
        return CompilationMapper.INSTANCE.mapToCompilationResp(compilationRepository.findByCompilationId(compilationId)
                .orElseThrow(() -> new NotFoundException("Компиляция с id=" + compilationId + " не найдена")));
    }

    @Override
    public CompilationListDto getCompilations(Boolean pinned, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        Page<Compilation> page;

        if (pinned != null) {
            booleanBuilder.and(QCompilation.compilation.pinned.eq(pinned));
        }

        if (booleanBuilder.getValue() != null) {
            page = compilationRepository.findAll(booleanBuilder.getValue(), pageable);
        } else {
            page = compilationRepository.findAll(pageable);
        }

        return CompilationListDto
                .builder()
                .compilations(CompilationMapper.INSTANCE.mapToCompilationRespList(page.getContent()))
                .build();
    }
}