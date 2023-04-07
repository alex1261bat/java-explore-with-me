package ru.practicum.service.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.comment.dto.CommentDto;
import ru.practicum.service.comment.dto.CommentMapper;
import ru.practicum.service.comment.dto.CommentShortDto;
import ru.practicum.service.comment.model.Comment;
import ru.practicum.service.comment.model.CommentState;
import ru.practicum.service.comment.repository.CommentRepository;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.model.State;
import ru.practicum.service.event.repository.EventRepository;
import ru.practicum.service.exceptions.NotFoundException;
import ru.practicum.service.exceptions.ValidationException;
import ru.practicum.service.request.model.Request;
import ru.practicum.service.request.model.RequestStatus;
import ru.practicum.service.request.repository.RequestRepository;
import ru.practicum.service.user.model.User;
import ru.practicum.service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper mapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long eventId, CommentDto newComment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Event event;

        if (user.getCommentsIsBlocked()) {
            throw new ValidationException("У пользователя с id=" + userId + " заблокированы комментарии");
        }

        event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Невозможно добавить комментарий к событию в статусе " + event.getState());
        }

        if (!event.getInitiator().getUserId().equals(userId)) {
            Request request = requestRepository.findByRequesterUserIdAndEvent(userId, event);

            if (request == null || request.getStatus() != RequestStatus.CONFIRMED) {
                throw new ValidationException("Невозможно добавить комментарий к событию, в котором не принимали участие");
            }
        }

        Comment comment = mapper.mapToComment(newComment);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        comment.setState(CommentState.NOT_EDITED);

        return mapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateUserComment(Long userId, Long commentId, CommentDto updateComment) {
        Comment comment = findByCommentIdAndAuthorId(commentId, userId);

        if (LocalDateTime.now().isAfter(comment.getCreated().plusHours(1))) {
            throw new ValidationException("Нельзя обновить комментарий, созданный более 1 часа назад");
        }

        comment.setState(CommentState.EDITED);
        comment.setEdited(LocalDateTime.now());

        return mapper.mapToCommentDto(commentRepository.save(mapper.mapToComment(updateComment, comment)));
    }

    @Override
    @Transactional
    public void deleteUserComment(Long commentId, Long userId) {
        Comment comment = findByCommentIdAndAuthorId(commentId, userId);

        if (LocalDateTime.now().isAfter(comment.getCreated().plusHours(1))) {
            throw new ValidationException("Нельзя удалить комментарий, созданный более 1 часа назад");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteCommentAdmin(Long commentId, Long userId) {
        findByCommentIdAndAuthorId(commentId, userId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentDto updateCommentAdmin(Long userId, Long commentId, CommentDto updateComment) {
        Comment comment = findByCommentIdAndAuthorId(commentId, userId);
        comment.setState(CommentState.EDITED);
        comment.setEdited(LocalDateTime.now());

        return mapper.mapToCommentDto(commentRepository.save(mapper.mapToComment(updateComment, comment)));
    }

    @Override
    public CommentDto getUserComment(Long userId, Long commentId) {
        Comment comment = findByCommentIdAndAuthorId(commentId, userId);

        return mapper.mapToCommentDto(comment);
    }

    @Override
    public List<CommentShortDto> getUserComments(Long userId, Long eventId) {
        return commentRepository.findAllByAuthorUserIdAndEventEventId(userId, eventId).stream()
                        .map(mapper::mapToCommentShortDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentShortDto> getCommentsPublic(Long eventId) {
        return commentRepository.findAllByEventEventId(eventId).stream()
                        .map(mapper::mapToCommentShortDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentPublic(Long commentId) {
        return mapper.mapToCommentDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=" + commentId + " не найден")));
    }

    private Comment findByCommentIdAndAuthorId(Long commentId, Long userId) {
        return commentRepository.findByIdAndAuthorUserId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=" + commentId + " пользователя с id=" +
                        userId + " не найден"));
    }
}
