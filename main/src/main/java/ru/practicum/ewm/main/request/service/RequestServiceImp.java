package ru.practicum.ewm.main.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.event.model.EventState;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.exceptions.NotFoundException;
import ru.practicum.ewm.main.exceptions.ValidationException;
import ru.practicum.ewm.main.request.dto.RequestDto;
import ru.practicum.ewm.main.request.dto.RequestListDto;
import ru.practicum.ewm.main.request.dto.RequestMapper;
import ru.practicum.ewm.main.request.model.Request;
import ru.practicum.ewm.main.request.model.RequestStatus;
import ru.practicum.ewm.main.request.repository.RequestRepository;
import ru.practicum.ewm.main.user.model.User;
import ru.practicum.ewm.main.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RequestServiceImp implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найдено"));
        validateCreatingRequest(event, userId);

        if (event.getParticipantLimit() == 0 || event.getParticipantLimit() > event.getConfirmedRequests()) {
            Request newRequest = prepareRequest(event, requester);

            if (!event.getRequestModeration()) {
                newRequest.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
            } else {
                newRequest.setStatus(RequestStatus.PENDING);
            }
            return requestMapper.mapToRequestDto(requestRepository.save(newRequest));
        } else {
            throw new ValidationException("Достигнут лимит запросов");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RequestListDto getUserRequests(Long userId) {
        if (userRepository.existsById(userId)) {
            return RequestListDto.builder()
                    .requests(requestMapper.mapToRequestDtoList(
                            requestRepository.findAllByRequesterUserId(userId)))
                    .build();
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найдено");
        }
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найдено");
        }

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id=" + requestId + " не найден"));
        Event event = request.getEvent();

        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            request.setStatus(RequestStatus.CANCELED);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.CANCELED);
        }

        return requestMapper.mapToRequestDto(requestRepository.save(request));
    }

    private Request prepareRequest(Event event, User requester) {
        Request newRequest = new Request();
        newRequest.setEvent(event);
        newRequest.setRequester(requester);

        return newRequest;
    }

    private void validateCreatingRequest(Event event, Long userId) {
        if (event.getInitiator().getUserId().equals(userId)) {
            throw new ValidationException("Вы не можете создать запрос на участие в вашем собственном мероприятии");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ValidationException("Вы не можете участвовать в неопубликованном мероприятии");
        }
    }
}
