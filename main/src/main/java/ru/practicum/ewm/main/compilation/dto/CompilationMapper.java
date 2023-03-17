package ru.practicum.ewm.main.compilation.dto;

import org.mapstruct.*;
import ru.practicum.ewm.main.compilation.model.Compilation;
import ru.practicum.ewm.main.event.dto.EventMapper;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = EventMapper.class)
public interface CompilationMapper {

    @Mapping(target = "events", ignore = true)
    Compilation mapToCompilation(NewCompilationDto compilationDto);

    @Mapping(target = "id", source = "compilationId")
    CompilationResponseDto mapToCompilationResp(Compilation compilation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", ignore = true)
    Compilation mapToCompilation(UpdateCompilationRequestDto updateCompilation, @MappingTarget Compilation compilation);

    List<CompilationResponseDto> mapToCompilationRespList(List<Compilation> compilations);
}
