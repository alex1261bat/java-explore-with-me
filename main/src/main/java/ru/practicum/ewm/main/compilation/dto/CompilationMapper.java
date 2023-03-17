package ru.practicum.ewm.main.compilation.dto;

import org.mapstruct.*;

import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.main.compilation.model.Compilation;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompilationMapper {
    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    @Mapping(target = "events", ignore = true)
    Compilation mapToCompilation(NewCompilationDto compilationDto);

    CompilationResponseDto mapToCompilationResp(Compilation compilation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", ignore = true)
    Compilation mapToCompilation(UpdateCompilationRequestDto updateCompilation, @MappingTarget Compilation compilation);

    List<CompilationResponseDto> mapToCompilationRespList(List<Compilation> compilations);
}
