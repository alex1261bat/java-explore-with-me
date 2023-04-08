package ru.practicum.service.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.comment.model.CommentState;

@Data
@Builder
public class CommentShortDto {
    private String text;
    private String authorName;
    private CommentState state;
    private String created;
}
