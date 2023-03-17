package ru.practicum.ewm.main.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.main.event.dto.*;
import ru.practicum.ewm.main.request.dto.EventRequestStatusUpdateDto;
import ru.practicum.ewm.main.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.ewm.main.request.dto.RequestListDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDto addNewEvent(Long userId, NewEventDto eventDto);

    ListEventShortDto getPrivateUserEvents(Long userId, Pageable pageable);

    EventDto getPrivateUserEvent(Long userId, Long eventId);

    EventDto updateEventUser(Long userId, Long eventId, UpdateEventRequestDto updateEvent);

    ListEventDto getEventsByFiltersForAdmin(List<Long> ids, List<String> states, List<Long> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventDto updateEventAdmin(Long eventId, UpdateEventRequestDto updateEvent);

    RequestListDto getUserEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResultDto approveRequests(Long userId, Long eventId, EventRequestStatusUpdateDto requests);

    EventDto getEventByIdPublic(Long eventId, HttpServletRequest servlet);

    ListEventShortDto getEventsByFiltersPublic(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                               Pageable pageable, HttpServletRequest servlet);
}
