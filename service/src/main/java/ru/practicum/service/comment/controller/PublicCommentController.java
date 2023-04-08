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
@RequestMapping("/comments")
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentShortDto>> getCommentsPublic(@RequestParam @Positive Long eventId) {
        log.info("Получение комментария для eventId={}", eventId);

        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsPublic(eventId));
    }

    @GetMapping("{commentId}")
    public ResponseEntity<CommentDto> getCommentPublic(@PathVariable @Positive Long commentId) {
        log.info("Получение комментария с id={}", commentId);

        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentPublic(commentId));
    }
}
