package ru.practicum.ewm.main.request.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateDto {
    private List<Long> requestIds;
    private String status;
}
