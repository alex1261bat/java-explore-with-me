package ru.practicum.ewm.main.user.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class UserListDto {
    @JsonValue
    private List<UserDto> users;
}
