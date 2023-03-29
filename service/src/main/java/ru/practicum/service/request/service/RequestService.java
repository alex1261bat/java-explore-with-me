package ru.practicum.service.request.service;

import ru.practicum.service.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> getUserRequests(Long userId);

    RequestDto cancelRequest(Long userId, Long eventId);
}
