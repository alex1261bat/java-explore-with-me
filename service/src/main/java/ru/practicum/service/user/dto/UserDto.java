package ru.practicum.service.user.dto;

import jdk.jfr.BooleanFlag;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotNull
    private String name;
    @Email
    @NotNull
    private String email;
    @BooleanFlag
    private Boolean commentsIsBlocked;
}
