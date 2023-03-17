package ru.practicum.ewm.statistic.repository;

import dto.StatisticDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.statistic.model.HitEndpoint;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<HitEndpoint, Long> {

    @Query("SELECT new dto.StatisticDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM HitEndpoint AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<StatisticDto> getStat(@Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);

    @Query("SELECT new dto.StatisticDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM HitEndpoint AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<StatisticDto> getUrisStat(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query("SELECT new dto.StatisticDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM HitEndpoint AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatisticDto> getUniqueStat(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);

    @Query("SELECT new dto.StatisticDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM HitEndpoint AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatisticDto> getUniqueUrisStat(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end,
                                         @Param("uris") List<String> uris);
}
