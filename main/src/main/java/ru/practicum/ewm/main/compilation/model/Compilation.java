package ru.practicum.ewm.main.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.main.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {
    @Id
    @Column(name = "COMPILATION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long compilationId;
    @ManyToMany
    @JoinTable(
            name = "event_compilations",
            joinColumns = @JoinColumn(name = "COMPILATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID")

    )
    private Set<Event> events;
    @Column(name = "PINNED")
    private Boolean pinned = Boolean.FALSE;
    @Column(name = "TITLE", nullable = false)
    private String title;
}
