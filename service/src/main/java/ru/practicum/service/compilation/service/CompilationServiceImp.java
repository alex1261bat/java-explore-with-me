package ru.practicum.service.compilation.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.compilation.dto.*;
import ru.practicum.service.compilation.model.Compilation;
import ru.practicum.service.compilation.model.QCompilation;
import ru.practicum.service.compilation.repository.CompilationRepository;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.repository.EventRepository;
import ru.practicum.service.exceptions.NotFoundException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompilationServiceImp implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    @Transactional
    public CompilationResponseDto addCompilation(NewCompilationDto compilationDto) {
        Set<Event> findEvents = eventRepository.findAllByEventIdIn(compilationDto.getEvents());
        Compilation compilation = compilationMapper.mapToCompilation(compilationDto);
        compilation.setEvents(findEvents);

        return compilationMapper.mapToCompilationResp(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compilationId) {
        if (compilationRepository.existsById(compilationId)) {
            compilationRepository.deleteById(compilationId);
        } else {
            throw new NotFoundException("Компиляция с id=" + compilationId + " не найдена");
        }
    }

    @Override
    @Transactional
    public CompilationResponseDto updateCompilation(Long compilationId, UpdateCompilationRequestDto updateCompilation) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Компиляция с id=" + compilationId + " не найдена"));
        Set<Event> findEvents = eventRepository.findAllByEventIdIn(updateCompilation.getEvents());
        compilation = compilationMapper.mapToCompilation(updateCompilation, compilation);
        compilation.setEvents(findEvents);

        return compilationMapper.mapToCompilationResp(compilationRepository.save(compilation));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationResponseDto getCompilation(Long compilationId) {
        return compilationMapper.mapToCompilationResp(compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Компиляция с id=" + compilationId + " не найдена")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationResponseDto> getCompilations(Boolean pinned, Pageable pageable) {
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

        return compilationMapper.mapToCompilationRespList(page.getContent());
    }
}
