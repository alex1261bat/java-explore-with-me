package ru.practicum.service.request.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateResultDto {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
