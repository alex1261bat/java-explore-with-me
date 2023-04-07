package ru.practicum.service.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.comment.dto.CommentDto;
import ru.practicum.service.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/admin/users")
public class AdminCommentController {
    private final CommentService commentService;

    @DeleteMapping("{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteCommentAdmin(@PathVariable @Positive Long userId,
                                                   @PathVariable @Positive Long commentId) {
        log.info("Удаление администратором комментария с userId={} и commentId={}", userId, commentId);
        commentService.deleteCommentAdmin(commentId, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentAdmin(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long commentId,
                                                         @RequestBody @Valid CommentDto commentDto) {
        log.info("Обновление администратором комментария с commentId={} и userId={}: {}", commentId, userId, commentDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateCommentAdmin(userId, commentId, commentDto));
    }
}
