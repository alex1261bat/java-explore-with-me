package ru.practicum.service.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.user.model.UserCommentsStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class UserCommentsStatusDto {
    @NotNull
    private List<Long> userIds;
    @NotNull
    private UserCommentsStatus status;
}