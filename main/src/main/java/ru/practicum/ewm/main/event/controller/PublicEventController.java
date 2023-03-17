package ru.practicum.ewm.main.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.event.dto.EventDto;
import ru.practicum.ewm.main.event.dto.ListEventShortDto;
import ru.practicum.ewm.main.event.model.EventSort;
import ru.practicum.ewm.main.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;
    private static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable("id") @Min(1) Long eventId,
                                                 HttpServletRequest servlet) {
        log.info("Получить событие с id={}", eventId);

        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventByIdPublic(eventId, servlet));
    }

    @GetMapping
    public ResponseEntity<ListEventShortDto> getEventsPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMATTER) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMATTER) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest servlet) {
        log.info("Получить события общедоступные");

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsByFiltersPublic(
                        text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                        PageRequest.of(from / size, size,
                                Sort.by(EventSort.getSortField(sort)).ascending()), servlet));
    }
}
