package ru.practicum.ewm.main.event.dto;

import org.mapstruct.*;
import ru.practicum.ewm.main.category.dto.CategoryMapper;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.model.EventState;
import ru.practicum.ewm.main.user.dto.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {CategoryMapper.class, UserMapper.class})
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(source = "eventDate", target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Event mapToEvent(NewEventDto newEventDto);

    @Mapping(source = "eventId", target = "id")
    @Mapping(source = "eventDate", target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "initiator.userId", target = "initiator.id")
    @Mapping(source = "createdOn", target = "createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "publishedOn", target = "publishedOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventDto mapToEventDto(Event event);

    @Mapping(source = "eventDate", target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "initiator.userId", target = "initiator.id")
    @Mapping(source = "eventId", target = "id")
    EventShortDto mapToEventShortDto(Event event);

    List<EventShortDto> mapToListEventShortDto(List<Event> events);

    List<EventDto> mapToListEventDto(List<Event> events);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(source = "eventDate", target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Event mapToEvent(UpdateEventRequestDto updateEvent, @MappingTarget Event event);

    EventState mapToState(String state);

    List<EventState> mapToListStates(List<String> states);
}
