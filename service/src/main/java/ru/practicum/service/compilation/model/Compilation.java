package ru.practicum.service.compilation.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.service.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPILATION_ID")
    private Long compilationId;
    @ManyToMany
    @JoinTable(name = "event_compilations",
               joinColumns = @JoinColumn(name = "COMPILATION_ID"),
               inverseJoinColumns = @JoinColumn(name = "EVENT_ID"))
    private Set<Event> events;
    @Column(name = "PINNED")
    private Boolean pinned = Boolean.FALSE;
    @Column(name = "TITLE", nullable = false)
    private String title;
}
