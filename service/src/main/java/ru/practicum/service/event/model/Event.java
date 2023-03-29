package ru.practicum.service.event.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.service.category.model.Category;
import ru.practicum.service.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private Long eventId;
    @Column(name = "ANNOTATION", nullable = false)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Column(name = "EVENT_DATE", nullable = false)
    private LocalDateTime eventDate;
    @Embedded
    private Location location;
    @Column(name = "PAID")
    private Boolean paid = Boolean.FALSE;
    @Column(name = "PARTICIPANT_LIMIT")
    private Integer participantLimit = 0;
    @Column(name = "REQUEST_MODERATION")
    private Boolean requestModeration = Boolean.TRUE;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "CREATED_ON", nullable = false)
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "INITIATOR")
    private User initiator;
    @Column(name = "PUBLISHED_ON")
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private State state = State.PENDING;
}
