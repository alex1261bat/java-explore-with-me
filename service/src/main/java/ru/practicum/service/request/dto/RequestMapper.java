package ru.practicum.service.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.service.request.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "event.eventId", target = "event")
    @Mapping(source = "requester.userId", target = "requester")
    @Mapping(source = "requestId", target = "id")
    RequestDto mapToRequestDto(Request request);

    List<RequestDto> mapToRequestDtoList(List<Request> requests);
}
