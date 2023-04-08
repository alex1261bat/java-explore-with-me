package ru.practicum.service.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.service.comment.model.CommentState;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    @NotBlank
    @Size(max = 5000)
    private String text;
    private CommentState state;
    private LocalDateTime created;
    private LocalDateTime edited;
    private String authorName;
    private String eventTitle;
}
