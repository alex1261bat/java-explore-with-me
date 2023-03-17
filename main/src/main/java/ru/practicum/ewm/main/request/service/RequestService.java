package ru.practicum.ewm.main.request.service;

import ru.practicum.ewm.main.request.dto.RequestDto;
import ru.practicum.ewm.main.request.dto.RequestListDto;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    RequestListDto getUserRequests(Long userId);

    RequestDto canceledRequest(Long userId, Long eventId);
}
