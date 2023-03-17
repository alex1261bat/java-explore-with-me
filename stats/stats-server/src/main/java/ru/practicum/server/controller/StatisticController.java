package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.config.*;
import ru.practicum.dto.HitEndpointDto;
import ru.practicum.dto.StatisticDto;
import ru.practicum.server.service.StatisticService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    public ResponseEntity<HitEndpointDto> saveStat(@NotNull @RequestBody HitEndpointDto hitEndpointDto) {
        log.info("Запрос на сохранение информации об обращении к эндпоинту {}", hitEndpointDto.getUri());

        return ResponseEntity.status(HttpStatus.CREATED).body(statisticService.saveStatistic(hitEndpointDto));
    }

    @GetMapping("/stats")
    public List<StatisticDto> getStat(@NotEmpty @DateValidator @RequestParam String start,
                                      @NotEmpty @DateValidator @RequestParam String end,
                                      @RequestParam(required = false) String[] uris,
                                      @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Запрос на получение статистики");

        return statisticService.getStatistic(start, end, uris, unique);
    }
}
