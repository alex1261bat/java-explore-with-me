package ru.practicum.service.request.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RequestListDto {
    @JsonValue
    private List<RequestDto> requests;
}
