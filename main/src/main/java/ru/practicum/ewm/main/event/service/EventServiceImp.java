package ru.practicum.ewm.main.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.category.model.Category;
import ru.practicum.ewm.main.category.repository.CategoryRepository;
import ru.practicum.ewm.main.event.dto.*;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.model.QEvent;
import ru.practicum.ewm.main.event.model.EventState;
import ru.practicum.ewm.main.event.model.EventStateAction;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.event.statisticClient.MainStatisticClient;
import ru.practicum.ewm.main.exceptions.NotFoundException;
import ru.practicum.ewm.main.exceptions.ValidationException;
import ru.practicum.ewm.main.request.dto.*;
import ru.practicum.ewm.main.request.model.RequestStatus;
import ru.practicum.ewm.main.request.repository.RequestRepository;
import ru.practicum.ewm.main.user.model.User;
import ru.practicum.ewm.main.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImp implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MainStatisticClient mainStatisticClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RequestRepository requestRepository;

    @Override
    public EventDto addNewEvent(Long userId, NewEventDto eventDto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Category category = categoryRepository.findByCategoryId(eventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Категория с id=" + eventDto.getCategory() + " не найдена"));
        Event newEvent = EventMapper.INSTANCE.mapToEvent(eventDto);

        if (newEvent.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new ValidationException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + eventDto.getEventDate());
        } else {
            newEvent.setInitiator(user);
            newEvent.setCategory(category);
            newEvent.setCreatedOn(LocalDateTime.now());

            return EventMapper.INSTANCE.mapToEventDto(eventRepository.save(newEvent));
        }
    }

    @Override
    public ListEventShortDto getPrivateUserEvents(Long userId, Pageable pageable) {
        if (userRepository.existsByUserId(userId)) {
            return ListEventShortDto
                    .builder()
                    .events(EventMapper.INSTANCE.mapToListEventShortDto(
                            eventRepository.findAllByInitiatorUserId(userId, pageable)))
                    .build();
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    public EventDto getPrivateUserEvent(Long userId, Long eventId) {
        if (userRepository.existsByUserId(userId)) {
            return EventMapper.INSTANCE.mapToEventDto(eventRepository.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено")));
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    public EventDto updateEventUser(Long userId, Long eventId, UpdateEventRequestDto updateEvent) {
        if (userRepository.existsByUserId(userId)) {
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

            if (event.getState().equals(EventState.PUBLISHED)) {
                throw new ValidationException("Могут быть изменены только ожидающие или отмененные события");
            }

            if (updateEvent.getCategory() != null) {
                event.setCategory(categoryRepository.findByCategoryId(updateEvent.getCategory()).orElseThrow(
                        () -> new NotFoundException("Категория с id=" + updateEvent.getCategory() + " не найдена")));
            }

            event.setState(EventStateAction.getState(updateEvent.getStateAction()));

            return EventMapper.INSTANCE.mapToEventDto(
                    eventRepository.save(EventMapper.INSTANCE.mapToEvent(updateEvent, event)));
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    public ListEventDto getEventsByFiltersForAdmin(List<Long> ids, List<String> states, List<Long> categories,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   Pageable pageable) {
        BooleanBuilder booleanBuilder = createQuery(ids, states, categories, rangeStart, rangeEnd);
        Page<Event> page;

        if (booleanBuilder.getValue() != null) {
            page = eventRepository.findAll(booleanBuilder, pageable);
        } else {
            page = eventRepository.findAll(pageable);
        }

        return ListEventDto
                .builder()
                .events(EventMapper.INSTANCE.mapToListEventDto(page.getContent()))
                .build();
    }

    @Override
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

        Event event = eventRepository.findByEventIdIs(eventId).orElseThrow(
                () -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        changeEventState(event, updateEvent.getStateAction());

        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryRepository.findByCategoryId(updateEvent.getCategory()).orElseThrow(
                    () -> new NotFoundException("Категория с id=" + event.getCategory() + " не найдена")));
        }

        return EventMapper.INSTANCE.mapToEventDto(
                eventRepository.save(EventMapper.INSTANCE.mapToEvent(updateEvent, event)));
    }

    @Override
    public RequestListDto getUserEventRequests(Long userId, Long eventId) {
        if (userRepository.existsByUserId(userId)) {
            Event event = eventRepository.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));

            return RequestListDto
                    .builder()
                    .requests(event.getRequests().stream()
                            .map(RequestMapper.INSTANCE::mapToRequestDto).collect(Collectors.toList()))
                    .build();
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    public EventRequestStatusUpdateResultDto approveRequests(Long userId, Long eventId,
                                                             EventRequestStatusUpdateDto requests) {
        if (userRepository.existsByUserId(userId)) {
            Event event = eventRepository.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));

            if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
                throw new ValidationException("Лимит участников достигнут");
            }

            List<RequestDto> confirmedRequests = new ArrayList<>();
            List<RequestDto> rejectedRequests = new ArrayList<>();
            moderationRequests(confirmedRequests, rejectedRequests, event, requests);

            return EventRequestStatusUpdateResultDto
                    .builder()
                    .confirmedRequests(confirmedRequests)
                    .rejectedRequests(rejectedRequests)
                    .build();
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    public EventDto getEventByIdPublic(Long eventId, HttpServletRequest httpServletRequest) {
        mainStatisticClient.add(httpServletRequest, "ewm-server");
        Event event = eventRepository.findByEventIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        event.setViews(mainStatisticClient.get(eventId));

        return EventMapper.INSTANCE.mapToEventDto(eventRepository.save(event));
    }

    @Override
    public ListEventShortDto getEventsByFiltersPublic(String text, List<Long> categories, Boolean paid,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                      Boolean onlyAvailable,
                                                      Pageable pageable, HttpServletRequest httpServletRequest) {
        mainStatisticClient.add(httpServletRequest, "ewm-server");
        BooleanBuilder booleanBuilder = createQuery(null, null, categories, rangeStart, rangeEnd);
        Page<Event> page;

        if (text != null) {
            booleanBuilder.and(QEvent.event.annotation.likeIgnoreCase(text))
                    .or(QEvent.event.description.likeIgnoreCase(text));
        }

        if (rangeStart == null && rangeEnd == null) {
            booleanBuilder.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }

        if (onlyAvailable) {
            booleanBuilder.and((QEvent.event.participantLimit.eq(0)))
                    .or(QEvent.event.participantLimit.gt(QEvent.event.confirmedRequests));
        }

        if (paid != null) {
            booleanBuilder.and(QEvent.event.paid.eq(paid));
        }

        if (booleanBuilder.getValue() != null) {
            page = eventRepository.findAll(booleanBuilder.getValue(), pageable);
        } else {
            page = eventRepository.findAll(pageable);
        }

        return ListEventShortDto
                .builder()
                .events(EventMapper.INSTANCE.mapToListEventShortDto(page.getContent()))
                .build();
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
                        .map(EventState::valueOf).collect(Collectors.toList())));
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

    private void moderationRequests(List<RequestDto> confirmedRequests,
                                    List<RequestDto> rejectedRequests,
                                    Event event, EventRequestStatusUpdateDto requests) {
        requestRepository.findAllByRequestIdIn(requests.getRequestIds()).stream()
                .peek(request -> {
                    if (request.getStatus().equals(RequestStatus.PENDING)) {
                        if (event.getParticipantLimit() == 0) {
                            request.setStatus(RequestStatus.CONFIRMED);
                            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        } else if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                            if (!event.getRequestModeration()) {
                                request.setStatus(RequestStatus.CONFIRMED);
                                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                            } else {
                                if (requests.getStatus().equals(RequestStatus.CONFIRMED.toString())) {
                                    request.setStatus(RequestStatus.CONFIRMED);
                                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                                } else {
                                    request.setStatus(RequestStatus.REJECTED);
                                }
                            }
                        } else {
                            request.setStatus(RequestStatus.REJECTED);
                        }
                    } else {
                        throw new ValidationException("Может только подтверждать PENDING запросы");
                    }
                })
                .map(RequestMapper.INSTANCE::mapToRequestDto).forEach(requestDto -> {
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
                if (event.getState().equals(EventState.PENDING)) {
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                } else {
                    throw new ValidationException("Неправильное состояние события:" + event.getState());
                }
            case CANCELED:
                if (event.getState().equals(EventState.PENDING)) {
                    event.setState(EventState.CANCELED);
                    break;
                } else {
                    throw new ValidationException("Неправильное состояние события: " + event.getState());
                }
        }
    }
}
