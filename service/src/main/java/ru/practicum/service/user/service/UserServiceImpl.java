package ru.practicum.service.user.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.exceptions.NotFoundException;
import ru.practicum.service.exceptions.ValidationException;
import ru.practicum.service.user.dto.UserCommentsStatusDto;
import ru.practicum.service.user.dto.UserDto;
import ru.practicum.service.user.dto.UserMapper;
import ru.practicum.service.user.model.QUser;
import ru.practicum.service.user.model.User;
import ru.practicum.service.user.model.UserCommentsStatus;
import ru.practicum.service.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        userDto.setCommentsIsBlocked(false);

        return userMapper.mapToUserDto(userRepository.save(userMapper.mapToUser(userDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
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

        return userMapper.mapToUserDto(page);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    @Override
    @Transactional
    public UserDto changeUserCommentsStatus(UserCommentsStatusDto userCommentsStatusDto) {
        User user = userRepository.findById(userCommentsStatusDto.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userCommentsStatusDto.getUserId()
                        + " не найден"));

        if (userCommentsStatusDto.getStatus().equals(UserCommentsStatus.BANNED)) {
            user.setCommentsIsBlocked(Boolean.TRUE);
        } else if (userCommentsStatusDto.getStatus().equals(UserCommentsStatus.UNBANNED)) {
            user.setCommentsIsBlocked(Boolean.FALSE);
        } else {
            throw new ValidationException("Недопустимый параметр запроса " + userCommentsStatusDto.getStatus());
        }

        return userMapper.mapToUserDto(userRepository.save(user));
    }
}
