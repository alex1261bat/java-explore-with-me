package ru.practicum.ewm.main.event.dto;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.main.event.model.Event;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "category", ignore = true)
    @Mapping(source = "eventDate", target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Event mapToEvent(NewEventDto newEventDto);

    EventDto mapToEventDto(Event event);

    List<EventShortDto> mapToListEventShortDto(List<Event> events);

    List<EventDto> mapToListEventDto(List<Event> events);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(source = "eventDate", target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Event mapToEvent(UpdateEventRequestDto updateEvent, @MappingTarget Event event);
}
