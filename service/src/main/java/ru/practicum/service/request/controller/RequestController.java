package ru.practicum.service.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.request.dto.RequestDto;
import ru.practicum.service.request.service.RequestService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/users")
public class RequestController {
    private final RequestService requestService;

    @PostMapping("{userId}/requests")
    public ResponseEntity<RequestDto> createRequest(@PathVariable @Min(1) Long userId,
                                                    @RequestParam @Min(1) Long eventId) {
        log.info("Создание запроса с userId={} и eventId={}", userId, eventId);

        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.createRequest(userId, eventId));
    }

    @GetMapping("{userId}/requests")
    public ResponseEntity<List<RequestDto>> getUserRequests(@PathVariable @Min(1) Long userId) {
        log.info("Получение запроса с userId={}", userId);

        return ResponseEntity.status(HttpStatus.OK).body(requestService.getUserRequests(userId));
    }

    @PatchMapping("{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequest(@PathVariable @Min(1) Long userId,
                                                    @PathVariable @Min(1) Long requestId) {
        log.info("Отмена запроса с id={} и userId={}", requestId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(requestService.cancelRequest(userId, requestId));
    }
}
