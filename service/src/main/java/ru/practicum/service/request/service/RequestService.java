package ru.practicum.service.request.service;

import ru.practicum.service.request.dto.RequestDto;
import ru.practicum.service.request.dto.RequestListDto;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    RequestListDto getUserRequests(Long userId);

    RequestDto cancelRequest(Long userId, Long eventId);
}
