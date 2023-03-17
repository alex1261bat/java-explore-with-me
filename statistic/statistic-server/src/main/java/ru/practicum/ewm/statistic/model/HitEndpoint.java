package ru.practicum.ewm.statistic.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    private String app;
    @Column(name = "URI", nullable = false)
    private String uri;
    @Column(name = "IP", nullable = false)
    private String ip;
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime timestamp;
}
