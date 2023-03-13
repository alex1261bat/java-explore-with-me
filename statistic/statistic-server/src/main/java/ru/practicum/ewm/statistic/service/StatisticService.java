package ru.practicum.ewm.statistic.service;

import dto.HitEndpointDto;
import dto.StatisticDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.statistic.model.HitEndpoint;
import ru.practicum.ewm.statistic.model.StatisticMapper;
import ru.practicum.ewm.statistic.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {
    private final StatisticRepository statisticRepository;

    public HitEndpointDto saveStatistic(HitEndpoint hitEndpoint) {
        log.info("Сервис: Запрос на сохранение информации об обращении к эндпоинту {}", hitEndpoint.getUri());
        return StatisticMapper.INSTANCE.toEndpointHitDto(statisticRepository.save(hitEndpoint));
    }

    public List<StatisticDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("Контроллер: Запрос на получение статистики");
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
