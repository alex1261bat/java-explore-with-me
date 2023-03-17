package ru.practicum.ewm.main.request.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateResultDto {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
