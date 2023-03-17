package ru.practicum.ewm.main.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.main.request.model.Request;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "event.eventId", target = "event")
    @Mapping(source = "requester.userId", target = "requester")
    @Mapping(source = "requestId", target = "id")
    RequestDto mapToRequestDto(Request request);

    List<RequestDto> mapToRequestDtoList(List<Request> requests);
}
