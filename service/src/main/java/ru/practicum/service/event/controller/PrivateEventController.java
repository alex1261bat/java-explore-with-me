package ru.practicum.service.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.event.dto.EventDto;
import ru.practicum.service.event.dto.ListEventShortDto;
import ru.practicum.service.event.dto.NewEventDto;
import ru.practicum.service.event.dto.UpdateEventRequestDto;
import ru.practicum.service.event.service.EventService;
import ru.practicum.service.request.dto.EventRequestStatusUpdateDto;
import ru.practicum.service.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.service.request.dto.RequestListDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/users")
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping("{userId}/events")
    public ResponseEntity<EventDto> addEvent(@PathVariable @Min(1) Long userId,
                                             @RequestBody @Valid NewEventDto eventDto) {
        log.info("Добавить новое событие пользователя с id={}: {}", userId, eventDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addNewEvent(userId, eventDto));
    }

    @GetMapping("{userId}/events")
    public ResponseEntity<ListEventShortDto> getUserEvents(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                           @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                           @PathVariable @Min(1) Long userId) {
        log.info("Получить события пользователя с userId={}, from: {},size: {}", userId, from, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getPrivateUserEvents(userId, PageRequest.of(from / size, size)));
    }

    @GetMapping("{userId}/events/{eventId}")
    public ResponseEntity<EventDto> getUserEvent(@PathVariable @Min(1) Long userId,
                                                 @PathVariable @Min(1) Long eventId) {
        log.info("Получить событие с eventId={} и userId={}", eventId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(eventService.getPrivateUserEvent(userId, eventId));
    }

    @PatchMapping("{userId}/events/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable @Min(1) Long userId,
                                                @PathVariable @Min(1) Long eventId,
                                                @RequestBody @Valid UpdateEventRequestDto updateEvent) {
        log.info("Обновить событие с eventId={} и userId={} на event:{}", eventId, userId, updateEvent);

        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEventUser(userId, eventId, updateEvent));
    }

    @GetMapping("{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestListDto> getUserEventRequests(@PathVariable @Min(1) Long userId,
                                                               @PathVariable @Min(1) Long eventId) {
        log.info("Получить запросов для userId={} и eventId={}", userId, eventId);

        return ResponseEntity.status(HttpStatus.OK).body(eventService.getUserEventRequests(userId, eventId));
    }

    @PatchMapping("{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResultDto> approveRequests(@PathVariable @Min(1) Long userId,
                                                                             @PathVariable @Min(1) Long eventId,
                                                                             @RequestBody EventRequestStatusUpdateDto requests) {
        log.info("Обработка запроса для eventId={} и userId={}", eventId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(eventService.approveRequests(userId, eventId, requests));
    }
}
