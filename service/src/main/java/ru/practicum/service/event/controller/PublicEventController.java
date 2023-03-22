package ru.practicum.service.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.event.dto.EventDto;
import ru.practicum.service.event.dto.EventShortDto;
import ru.practicum.service.event.model.EventSort;
import ru.practicum.service.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;
    private static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable(name = "id") @Min(1) Long id,
                                                 HttpServletRequest servlet) {
        log.info("Получить событие с id={}", id);

        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventByIdPublic(id, servlet));
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsPublic(
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
                .body(eventService.getEventsByFiltersPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                        PageRequest.of(from / size, size, Sort.by(EventSort.getSortField(sort)).ascending()),
                        servlet));
    }
}
