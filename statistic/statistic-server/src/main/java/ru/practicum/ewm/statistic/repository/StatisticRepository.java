package ru.practicum.ewm.statistic.repository;

import dto.StatisticDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.statistic.model.HitEndpoint;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<HitEndpoint, Long> {

    @Query("select new dto.StatisticDto(e.app, e.uri, count(distinct e.ip)) " +
            "from HitEndpoint e " +
            "where e.timestamp between ?2 and ?3 " +
            "and e.uri in ?1 " +
            "group by e.app, e.uri")
    List<StatisticDto> findStatWithUnique(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select new dto.StatisticDto(e.app, e.uri, count(e.ip)) " +
            "from HitEndpoint e " +
            "where e.timestamp between ?2 and ?3 " +
            "and e.uri in ?1 " +
            "group by e.app, e.uri")
    List<StatisticDto> findStatNOtUnique(List<String> uris, LocalDateTime start, LocalDateTime end);
}
