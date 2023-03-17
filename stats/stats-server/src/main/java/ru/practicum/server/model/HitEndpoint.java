package ru.practicum.server.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "hits")
public class HitEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "APP", nullable = false)
    @NotNull
    private String app;
    @Column(name = "URI", nullable = false)
    @NotNull
    private String uri;
    @Column(name = "IP", nullable = false)
    @NotNull
    private String ip;
    @Column(name = "TIMESTAMP", nullable = false)
    @NotNull
    private LocalDateTime timestamp;
}
