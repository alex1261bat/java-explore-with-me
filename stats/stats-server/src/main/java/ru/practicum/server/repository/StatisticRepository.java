package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatisticDto;
import ru.practicum.server.model.HitEndpoint;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<HitEndpoint, Long> {

     @Query("SELECT new ru.practicum.dto.StatisticDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM HitEndpoint AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
     List<StatisticDto> getStat(@Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatisticDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM HitEndpoint AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<StatisticDto> getUrisStat(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") String[] uris);

    @Query("SELECT new ru.practicum.dto.StatisticDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM HitEndpoint AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatisticDto> getUniqueStat(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatisticDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM HitEndpoint AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatisticDto> getUniqueUrisStat(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end,
                                         @Param("uris") String[] uris);
}
