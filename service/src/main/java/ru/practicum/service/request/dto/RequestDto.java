package ru.practicum.service.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.request.model.RequestStatus;

@Data
@Builder
public class RequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private RequestStatus status;
    private String created;
}
