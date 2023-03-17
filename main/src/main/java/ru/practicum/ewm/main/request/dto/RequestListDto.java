package ru.practicum.ewm.main.request.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestListDto {
    @JsonValue
    private List<RequestDto> requests;
}
