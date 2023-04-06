package ru.practicum.service.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.service.user.dto.UserCommentsStatusDto;
import ru.practicum.service.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids, Pageable pageable);

    void deleteUser(Long userId);

    List<UserDto> changeUserCommentsStatus(UserCommentsStatusDto userCommentsStatusDto);
}
