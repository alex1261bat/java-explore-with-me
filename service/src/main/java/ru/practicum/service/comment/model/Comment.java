package ru.practicum.service.comment.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID", nullable = false)
    private Long id;
    @Column(name = "TEXT", nullable = false)
    private String text;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private CommentState state = CommentState.NOT_EDITED;
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
    @Column(name = "EDITED")
    private LocalDateTime edited;
    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID")
    private User author;
    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;
}
