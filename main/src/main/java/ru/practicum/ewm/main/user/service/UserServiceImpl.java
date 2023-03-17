package ru.practicum.ewm.main.user.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.exceptions.NotFoundException;
import ru.practicum.ewm.main.user.dto.UserDto;
import ru.practicum.ewm.main.user.dto.UserListDto;
import ru.practicum.ewm.main.user.dto.UserMapper;
import ru.practicum.ewm.main.user.model.User;
import ru.practicum.ewm.main.user.model.QUser;
import ru.practicum.ewm.main.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        return UserMapper.INSTANCE.mapToUserDto(userRepository.save(UserMapper.INSTANCE.mapToUser(userDto)));
    }

    @Override
    public UserListDto getUsers(List<Long> ids, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (ids != null && !ids.isEmpty()) {
            booleanBuilder.and(QUser.user.userId.in(ids));
        }

        Page<User> page;

        if (booleanBuilder.getValue() != null) {
            page = userRepository.findAll(booleanBuilder.getValue(), pageable);
        } else {
            page = userRepository.findAll(pageable);
        }

        return UserListDto.builder()
                .users(UserMapper.INSTANCE.mapToUserDto(page))
                .build();
    }

    @Override
    public void deleteUser(Long userId) {
        if (userRepository.existsByUserId(userId)) {
            userRepository.deleteByUserId(userId);
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }
}
