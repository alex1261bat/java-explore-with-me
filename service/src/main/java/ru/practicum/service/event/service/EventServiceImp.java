package ru.practicum.service.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatisticDto;
import ru.practicum.service.category.model.Category;
import ru.practicum.service.category.repository.CategoryRepository;
import ru.practicum.service.event.dto.*;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.model.EventStateAction;
import ru.practicum.service.event.model.QEvent;
import ru.practicum.service.event.model.State;
import ru.practicum.service.event.repository.EventRepository;
import ru.practicum.service.event.statsClient.ServiceStatsClient;
import ru.practicum.service.exceptions.NotFoundException;
import ru.practicum.service.exceptions.ValidationException;
import ru.practicum.service.request.dto.EventRequestStatusUpdateDto;
import ru.practicum.service.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.service.request.dto.RequestDto;
import ru.practicum.service.request.dto.RequestMapper;
import ru.practicum.service.request.model.Request;
import ru.practicum.service.request.model.RequestStatus;
import ru.practicum.service.request.repository.RequestRepository;
import ru.practicum.service.user.model.User;
import ru.practicum.service.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventServiceImp implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ServiceStatsClient serviceStatsClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public EventDto addNewEvent(Long userId, NewEventDto eventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Категория с id=" + eventDto.getCategory() + " не найдена"));
        Event newEvent = eventMapper.mapToEvent(eventDto);

        if (newEvent.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new ValidationException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + eventDto.getEventDate());
        } else {
            newEvent.setInitiator(user);
            newEvent.setCategory(category);
            newEvent.setCreatedOn(LocalDateTime.now());

            return eventMapper.mapToEventDto(eventRepository.save(newEvent));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getPrivateUserEvents(Long userId, Pageable pageable) {
        if (userRepository.existsById(userId)) {
            return eventMapper.mapToListEventShortDto(
                            eventRepository.findAllByInitiatorUserId(userId, pageable));
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getPrivateUserEvent(Long userId, Long eventId) {
        if (userRepository.existsById(userId)) {
            return eventMapper.mapToEventDto(eventRepository.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено")));
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional
    public EventDto updateEventUser(Long userId, Long eventId, UpdateEventRequestDto updateEvent) {
        if (userRepository.existsById(userId)) {
            LocalDateTime eventTime;

            if (updateEvent.getEventDate() != null) {
                eventTime = LocalDateTime.parse(updateEvent.getEventDate(), formatter);

                if (eventTime.isBefore(LocalDateTime.now().minusHours(2))) {
                    throw new ValidationException(
                            "Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                            "Value: " + eventTime);
                }
            }

            Event event = eventRepository.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));

            if (event.getState().equals(State.PUBLISHED)) {
                throw new ValidationException("Могут быть изменены только ожидающие или отмененные события");
            }

            if (updateEvent.getCategory() != null) {
                event.setCategory(categoryRepository.findById(updateEvent.getCategory()).orElseThrow(
                        () -> new NotFoundException("Категория с id=" + updateEvent.getCategory() + " не найдена")));
            }

            event.setState(EventStateAction.getState(updateEvent.getStateAction()));

            return eventMapper.mapToEventDto(eventRepository.save(eventMapper.mapToEvent(updateEvent, event)));
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getEventsByFiltersForAdmin(List<Long> ids, List<String> states, List<Long> categories,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     Pageable pageable) {
        BooleanBuilder booleanBuilder = createQuery(ids, states, categories, rangeStart, rangeEnd);
        Page<Event> page;

        if (booleanBuilder.getValue() != null) {
            page = eventRepository.findAll(booleanBuilder, pageable);
        } else {
            page = eventRepository.findAll(pageable);
        }

        return eventMapper.mapToListEventDto(page.getContent());
    }

    @Override
    @Transactional
    public EventDto updateEventAdmin(Long eventId, UpdateEventRequestDto updateEvent) {
        LocalDateTime eventTime;

        if (updateEvent.getEventDate() != null) {
            eventTime = LocalDateTime.parse(updateEvent.getEventDate(), formatter);

            if (eventTime.isBefore(LocalDateTime.now().minusHours(1))) {
                throw new ValidationException(
                        "Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                        "Value: " + eventTime);
            }
        }

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        changeEventState(event, updateEvent.getStateAction());

        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEvent.getCategory()).orElseThrow(
                    () -> new NotFoundException("Категория с id=" + event.getCategory() + " не найдена")));
        }

        return eventMapper.mapToEventDto(eventRepository.save(eventMapper.mapToEvent(updateEvent, event)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getUserEventRequests(Long userId, Long eventId) {
        if (userRepository.existsById(userId)) {
            Event event = eventRepository.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));

            return requestRepository.findAllByEventIs(event).stream()
                            .map(requestMapper::mapToRequestDto).collect(Collectors.toList());
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDto approveRequests(Long userId, Long eventId,
                                                             EventRequestStatusUpdateDto requests) {
        if (userRepository.existsById(userId)) {
            Event event = eventRepository.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));

            if (event.getParticipantLimit() <= requestRepository
                    .findAllByEventIsAndStatusIs(event, RequestStatus.CONFIRMED).size()) {
                throw new ValidationException("Лимит участников достигнут");
            }

            List<RequestDto> confirmedRequests = new ArrayList<>();
            List<RequestDto> rejectedRequests = new ArrayList<>();
            moderateRequests(confirmedRequests, rejectedRequests, event, requests);

            return EventRequestStatusUpdateResultDto.builder()
                    .confirmedRequests(confirmedRequests)
                    .rejectedRequests(rejectedRequests)
                    .build();
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional
    public EventDto getEventByIdPublic(Long eventId, HttpServletRequest httpServletRequest) {
        serviceStatsClient.postStatistic(httpServletRequest, "ewm-service");
        Event event = eventRepository.findByEventIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        EventDto eventDto = eventMapper.mapToEventDto(eventRepository.save(event));

        eventDto.setViews(serviceStatsClient.getStatistic(eventId));

        return eventDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByFiltersPublic(String text, List<Long> categories, Boolean paid,
                                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                        Boolean onlyAvailable, Pageable pageable,
                                                        HttpServletRequest httpServletRequest) {
        serviceStatsClient.postStatistic(httpServletRequest, "ewm-service");
        BooleanBuilder booleanBuilder = createQuery(null, null, categories, rangeStart, rangeEnd);
        Page<Event> page;
        List<Request> requestList = requestRepository.findAllByStatusIs(RequestStatus.CONFIRMED);

        if (text != null) {
            booleanBuilder.and(QEvent.event.annotation.likeIgnoreCase(text))
                    .or(QEvent.event.description.likeIgnoreCase(text));
        }

        if (rangeStart == null && rangeEnd == null) {
            booleanBuilder.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }

        if (paid != null) {
            booleanBuilder.and(QEvent.event.paid.eq(paid));
        }

        if (booleanBuilder.getValue() != null) {
            page = eventRepository.findAll(booleanBuilder.getValue(), pageable);
        } else {
            page = eventRepository.findAll(pageable);
        }

        List<Event> eventList = page.getContent();
        List<EventShortDto> eventShortDtoList = eventMapper.mapToListEventShortDto(eventList).stream()
                .peek(eventShortDto -> {
                    eventShortDto.setViews(getEventViews(eventShortDto.getId()));
                    eventShortDto.setConfirmedRequests((int) requestList.stream()
                            .filter(request -> request.getEvent().getEventId().equals(eventShortDto.getId())).count());
                })
                .collect(Collectors.toList());

        if (onlyAvailable) {
            return findOnlyAvailable(eventShortDtoList, eventList);
        } else {
            return eventShortDtoList;
        }
    }

    private long getEventViews(Long eventId) {
        List<StatisticDto> statisticDtoList = serviceStatsClient.getAllStatistic();
        long views = 0L;

        if (statisticDtoList != null && !statisticDtoList.isEmpty()) {
            views = statisticDtoList.stream()
                    .filter(statisticDto -> statisticDto.getUri().contains(eventId.toString()))
                    .collect(Collectors.toList())
                    .get(0)
                    .getHits();
        }

        return views;
    }

    private List<EventShortDto> findOnlyAvailable(List<EventShortDto> eventShortDtoList, List<Event> eventList) {
        List<EventShortDto> eventShortDtos = new ArrayList<>();

        eventShortDtoList.forEach(eventShortDto -> {
            for (Event event : eventList) {
                if (eventShortDto.getId().equals(event.getEventId())
                        && (event.getParticipantLimit() == 0
                        || (event.getParticipantLimit() > eventShortDto.getConfirmedRequests()))) {
                        eventShortDtos.add(eventShortDto);
                }
            }
        });

        return eventShortDtos;
    }

    private BooleanBuilder createQuery(List<Long> ids, List<String> states, List<Long> categories,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (ids != null && !ids.isEmpty()) {
            booleanBuilder.and(QEvent.event.initiator.userId.in(ids));
        }

        if (states != null && !states.isEmpty()) {
            try {
                booleanBuilder.and(QEvent.event.state.in(states.stream()
                        .map(State::valueOf).collect(Collectors.toList())));
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }

        if (categories != null && !categories.isEmpty()) {
            booleanBuilder.and(QEvent.event.category.categoryId.in(categories));
        }

        if (rangeStart != null) {
            booleanBuilder.and(QEvent.event.eventDate.after(rangeStart));
        }

        if (rangeEnd != null) {
            booleanBuilder.and(QEvent.event.eventDate.before(rangeEnd));
        }

        return booleanBuilder;
    }

    private void moderateRequests(List<RequestDto> confirmedRequests,
                                  List<RequestDto> rejectedRequests,
                                  Event event, EventRequestStatusUpdateDto requests) {
        AtomicInteger eventConfirmedRequests = new AtomicInteger(requestRepository
                .findAllByEventIsAndStatusIs(event, RequestStatus.CONFIRMED).size());

        requestRepository.findAllByRequestIdIn(requests.getRequestIds()).stream()
                .peek(request -> {
                    if (request.getStatus().equals(RequestStatus.PENDING)) {
                        if (event.getParticipantLimit() == 0) {
                            request.setStatus(RequestStatus.CONFIRMED);
                            requestRepository.save(request);
                        } else if (event.getParticipantLimit() > eventConfirmedRequests.get()) {
                            if (!event.getRequestModeration()) {
                                request.setStatus(RequestStatus.CONFIRMED);
                                requestRepository.save(request);
                                eventConfirmedRequests.set(eventConfirmedRequests.get() + 1);
                            } else {
                                if (requests.getStatus().equals(RequestStatus.CONFIRMED.toString())) {
                                    request.setStatus(RequestStatus.CONFIRMED);
                                    requestRepository.save(request);
                                    eventConfirmedRequests.set(eventConfirmedRequests.get() + 1);
                                } else {
                                    request.setStatus(RequestStatus.REJECTED);
                                    requestRepository.save(request);
                                }
                            }
                        } else {
                            request.setStatus(RequestStatus.REJECTED);
                            requestRepository.save(request);
                        }
                    } else {
                        throw new ValidationException("Может только подтверждать PENDING запросы");
                    }
                })
                .map(requestMapper::mapToRequestDto)
                .forEach(requestDto -> {
                    if (requestDto.getStatus().equals(RequestStatus.CONFIRMED)) {
                        confirmedRequests.add(requestDto);
                    } else {
                        rejectedRequests.add(requestDto);
                    }
                });
    }

    private void changeEventState(Event event, String actionState) {
        switch (EventStateAction.getState(actionState)) {
            case PUBLISHED:
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                } else {
                    throw new ValidationException("Неправильное состояние события:" + event.getState());
                }
            case CANCELED:
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.CANCELED);
                    break;
                } else {
                    throw new ValidationException("Неправильное состояние события: " + event.getState());
                }
        }
    }
}
