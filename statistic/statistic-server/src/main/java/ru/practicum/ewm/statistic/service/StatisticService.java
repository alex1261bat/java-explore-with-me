package ru.practicum.ewm.statistic.service;

import dto.HitEndpointDto;
import dto.StatisticDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.statistic.model.StatisticMapper;
import ru.practicum.ewm.statistic.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository statisticRepository;
    private final StatisticMapper statisticMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public HitEndpointDto saveStatistic(HitEndpointDto hitEndpointDto) {
        return statisticMapper.toHitEndpointDto(
                statisticRepository.save(statisticMapper.toHitEndpoint(hitEndpointDto)));
    }

    @Transactional(readOnly = true)
    public List<StatisticDto> getStatistic(String start, String end, List<String> uris, Boolean unique) {
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
