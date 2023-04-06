package ru.practicum.service.comment.dto;

import org.mapstruct.*;
import ru.practicum.service.comment.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment mapToComment(CommentDto newComment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment mapToComment(CommentDto updateComment, @MappingTarget Comment comment);

    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "event.title", target = "eventTitle")
    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CommentDto mapToCommentDto(Comment comment);

    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CommentShortDto mapToCommentShortDto(Comment comment);

    List<CommentShortDto> mapToSetCommentShort(List<Comment> comments);
}
