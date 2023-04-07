package ru.practicum.service.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.user.model.UserCommentsStatus;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserCommentsStatusDto {
    @NotNull
    private Long userId;
    @NotNull
    private UserCommentsStatus status;
}
