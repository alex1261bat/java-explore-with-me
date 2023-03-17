package ru.practicum.ewm.main.request.dto;

import lombok.*;
import ru.practicum.ewm.main.request.model.RequestStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private RequestStatus status;
    private String created;
}
