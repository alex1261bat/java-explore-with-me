package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitEndpointDto;
import ru.practicum.dto.StatisticDto;
import ru.practicum.server.model.StatisticMapper;
import ru.practicum.server.repository.StatisticRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatisticService {
    private final StatisticRepository statisticRepository;
    private final StatisticMapper statisticMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @NotNull
    @Transactional
    public HitEndpointDto saveStatistic(@NotNull HitEndpointDto hitEndpointDto) {
        return statisticMapper.toHitEndpointDto(
                statisticRepository.save(statisticMapper.toHitEndpoint(hitEndpointDto)));
    }

    @NotNull
    @Transactional(readOnly = true)
    public List<StatisticDto> getStatistic(@NotNull String start, @NotNull String end,
                                           String[] uris, @NotNull Boolean unique) {
        LocalDateTime parseStart = LocalDateTime.parse(start, formatter);
        LocalDateTime parseEnd = LocalDateTime.parse(end, formatter);

        if (unique) {
            return uris == null
                    ? statisticRepository.getUniqueStat(parseStart, parseEnd)
                    : statisticRepository.getUniqueUrisStat(parseStart, parseEnd, uris);
        } else {
            return uris == null
                    ? statisticRepository.getStat(parseStart, parseEnd)
                    : statisticRepository.getUrisStat(parseStart, parseEnd, uris);
        }
    }
}
