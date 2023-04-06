package ru.practicum.service.comment.service;

import ru.practicum.service.comment.dto.CommentDto;
import ru.practicum.service.comment.dto.CommentShortDto;

import java.util.List;

public interface CommentService {

    CommentDto addComment(Long userId, Long eventId, CommentDto newComment);

    CommentDto updateUserComment(Long userId, Long commentId, CommentDto updateComment);

    void deleteUserComment(Long commentId, Long userId);

    void deleteCommentAdmin(Long commentId, Long userId);

    CommentDto updateCommentAdmin(Long userId, Long commentId, CommentDto updateComment);

    CommentDto getUserComment(Long userId, Long commentId);

    CommentDto getCommentPublic(Long commentId);

    List<CommentShortDto> getUserComments(Long userId, Long eventId);

    List<CommentShortDto> getCommentsPublic(Long eventId);
}
