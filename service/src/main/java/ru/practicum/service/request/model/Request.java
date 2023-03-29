package ru.practicum.service.request.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_ID")
    private Long requestId;
    @Column
    private LocalDateTime created = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "EVENT_ID", nullable = false, unique = true)
    private Event event;
    @ManyToOne
    @JoinColumn(name = "REQUESTER_ID", nullable = false, unique = true)
    private User requester;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private RequestStatus status = RequestStatus.PENDING;
}
