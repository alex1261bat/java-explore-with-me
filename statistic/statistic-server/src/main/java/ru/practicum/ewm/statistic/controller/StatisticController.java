package ru.practicum.ewm.statistic.controller;

import dto.HitEndpointDto;
import dto.StatisticDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.statistic.service.StatisticService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    public ResponseEntity<HitEndpointDto> saveStat(@RequestBody @Valid HitEndpointDto hitEndpointDto) {
        log.info("Запрос на сохранение информации об обращении к эндпоинту {}", hitEndpointDto.getUri());

        return ResponseEntity.status(HttpStatus.CREATED).body(statisticService.saveStatistic(hitEndpointDto));
    }

    @GetMapping("/stats")
    public List<StatisticDto> getStat(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Запрос на получение статистики");

        return statisticService.getStatistic(start, end, uris, unique);
    }
}
