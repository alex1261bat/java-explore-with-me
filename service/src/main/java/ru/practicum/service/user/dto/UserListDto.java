package ru.practicum.service.user.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserListDto {
    @JsonValue
    private List<UserDto> users;
}
