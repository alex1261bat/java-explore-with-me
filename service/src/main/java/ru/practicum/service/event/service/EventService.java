package ru.practicum.service.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.service.event.dto.EventDto;
import ru.practicum.service.event.dto.EventShortDto;
import ru.practicum.service.event.dto.NewEventDto;
import ru.practicum.service.event.dto.UpdateEventRequestDto;
import ru.practicum.service.request.dto.EventRequestStatusUpdateDto;
import ru.practicum.service.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.service.request.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDto addNewEvent(Long userId, NewEventDto eventDto);

    List<EventShortDto> getPrivateUserEvents(Long userId, Pageable pageable);

    EventDto getPrivateUserEvent(Long userId, Long eventId);

    EventDto updateEventUser(Long userId, Long eventId, UpdateEventRequestDto updateEvent);

    List<EventDto> getEventsByFiltersForAdmin(List<Long> ids, List<String> states, List<Long> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventDto updateEventAdmin(Long eventId, UpdateEventRequestDto updateEvent);

    List<RequestDto> getUserEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResultDto approveRequests(Long userId, Long eventId, EventRequestStatusUpdateDto requests);

    EventDto getEventByIdPublic(Long eventId, HttpServletRequest servlet);

    List<EventShortDto> getEventsByFiltersPublic(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                               Pageable pageable, HttpServletRequest servlet);
}
