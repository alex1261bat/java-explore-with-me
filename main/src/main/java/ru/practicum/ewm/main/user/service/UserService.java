package ru.practicum.ewm.main.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.main.user.dto.UserDto;
import ru.practicum.ewm.main.user.dto.UserListDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserListDto getUsers(List<Long> ids, Pageable pageable);

    void deleteUser(Long userId);
}
