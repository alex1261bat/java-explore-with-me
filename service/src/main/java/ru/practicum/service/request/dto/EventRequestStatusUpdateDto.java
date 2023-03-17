package ru.practicum.service.request.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateDto {
    private List<Long> requestIds;
    private String status;
}
