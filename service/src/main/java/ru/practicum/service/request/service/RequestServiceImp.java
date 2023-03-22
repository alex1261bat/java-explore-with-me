package ru.practicum.service.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.model.State;
import ru.practicum.service.event.repository.EventRepository;
import ru.practicum.service.exceptions.NotFoundException;
import ru.practicum.service.exceptions.ValidationException;
import ru.practicum.service.request.dto.RequestDto;
import ru.practicum.service.request.dto.RequestMapper;
import ru.practicum.service.request.model.Request;
import ru.practicum.service.request.model.RequestStatus;
import ru.practicum.service.request.repository.RequestRepository;
import ru.practicum.service.user.model.User;
import ru.practicum.service.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
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
        int eventConfirmedRequests = requestRepository
                .findAllByEventIsAndStatusIs(event, RequestStatus.CONFIRMED).size();

        if (event.getParticipantLimit() == 0 || event.getParticipantLimit() > eventConfirmedRequests) {
            Request newRequest = prepareRequest(event, requester);

            if (!event.getRequestModeration()) {
                newRequest.setStatus(RequestStatus.CONFIRMED);
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
    public List<RequestDto> getUserRequests(Long userId) {
        if (userRepository.existsById(userId)) {
            return requestMapper.mapToRequestDtoList(
                            requestRepository.findAllByRequesterUserId(userId));
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

        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            request.setStatus(RequestStatus.CANCELED);
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

        if (event.getState() != State.PUBLISHED) {
            throw new ValidationException("Вы не можете участвовать в неопубликованном мероприятии");
        }
    }
}
