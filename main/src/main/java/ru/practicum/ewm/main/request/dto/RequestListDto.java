package ru.practicum.ewm.main.request.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class RequestListDto {
    @JsonValue
    private List<RequestDto> requests;
}
