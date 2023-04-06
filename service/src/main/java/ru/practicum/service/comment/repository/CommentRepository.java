package ru.practicum.service.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.service.comment.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndAuthorUserId(Long commentId, Long userId);

    List<Comment> findAllByAuthorUserIdAndEventEventId(Long userId, Long eventId);

    List<Comment> findAllByEventEventId(Long eventId);
}
