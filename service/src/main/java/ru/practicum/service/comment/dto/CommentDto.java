package ru.practicum.service.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.comment.model.CommentState;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private CommentState state;
    private LocalDateTime created;
    private String authorName;
    private String eventTitle;
}
