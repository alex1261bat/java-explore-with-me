package ru.practicum.ewm.statistic.service;

import dto.HitEndpointDto;
import dto.StatisticDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.statistic.model.StatisticMapper;
import ru.practicum.ewm.statistic.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository statisticRepository;

    public HitEndpointDto saveStatistic(HitEndpointDto hitEndpointDto) {
        return StatisticMapper.INSTANCE.toHitEndpointDto(
                statisticRepository.save(StatisticMapper.INSTANCE.toHitEndpoint(hitEndpointDto)));
    }

    public List<StatisticDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null || uris.isEmpty()) {
            return new ArrayList<>();
        } else {
            if (unique) {
                return statisticRepository.findStatWithUnique(uris, start, end)
                        .stream()
                        .sorted(Comparator.comparing(StatisticDto::getHits).reversed()).collect(Collectors.toList());
            } else {
                return statisticRepository.findStatNOtUnique(uris, start, end)
                        .stream()
                        .sorted(Comparator.comparing(StatisticDto::getHits).reversed()).collect(Collectors.toList());
            }
        }
    }
}
