package ru.practicum.service.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.comment.dto.CommentDto;
import ru.practicum.service.comment.dto.CommentShortDto;
import ru.practicum.service.comment.service.CommentService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/users")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping("{userId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable @Positive Long userId,
                                                 @RequestParam @Positive Long eventId,
                                                 @RequestBody @Validated CommentDto comment) {
        log.info("Добавление нового комментария пользователя c id={} для события с id={}:{}", userId, eventId, comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(userId, eventId, comment));
    }

    @PatchMapping("{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateUserComment(@PathVariable @Positive Long userId,
                                                        @PathVariable @Positive Long commentId,
                                                        @RequestBody @Validated CommentDto updateComment) {
        log.info("Обновление комментария с id={} пользователем с id={}: {}", commentId, userId, updateComment);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateUserComment(userId, commentId, updateComment));
    }

    @DeleteMapping("{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteUserComment(@PathVariable @Positive Long userId,
                                                  @PathVariable @Positive Long commentId) {
        log.info("Удаление комментария с id={} пользователем с id={}", commentId, userId);
        commentService.deleteUserComment(commentId, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getUserComment(@PathVariable @Positive Long userId,
                                                     @PathVariable @Positive Long commentId) {
        log.info("Получение личного комментария с userId={} и commentId={}", userId, commentId);

        return ResponseEntity.status(HttpStatus.OK).body(commentService.getUserComment(userId, commentId));
    }

    @GetMapping("{userId}/events/{eventId}/comments")
    public ResponseEntity<List<CommentShortDto>> getUserComments(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long eventId) {
        log.info("Получение комментария с userId={} и commentId={}", userId, eventId);

        return ResponseEntity.status(HttpStatus.OK).body(commentService.getUserComments(userId, eventId));
    }
}
