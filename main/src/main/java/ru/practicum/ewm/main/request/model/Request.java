package ru.practicum.ewm.main.request.model;

import lombok.*;
import ru.practicum.ewm.main.event.model.Event;
import ru.practicum.ewm.main.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
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
    private RequestStatus status = RequestStatus.PENDING;
}
