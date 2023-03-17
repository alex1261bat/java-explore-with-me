package ru.practicum.client;

import dto.StatisticDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatisticClient {
    private final RestTemplate restTemplate;

    public  <T> void addHit(T body) {
        makeRequest(body);
    }

    public ResponseEntity<List<StatisticDto>> getStatistic(String path, Map<String, Object> parameters) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, getHeader());
        ResponseEntity<List<StatisticDto>> statisticServerResponse;

        try {
            statisticServerResponse = restTemplate.exchange(path, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<>() {
            }, parameters);
        } catch (HttpStatusCodeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return createClientResponse(statisticServerResponse);
    }

    private void createResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return;
        }

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status((response.getStatusCode()));

        if (response.hasBody()) {
            bodyBuilder.body(response.getBody());
            return;
        }

        bodyBuilder.build();
    }

    private ResponseEntity<List<StatisticDto>> createClientResponse(ResponseEntity<List<StatisticDto>> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status((response.getStatusCode()));

        if (response.hasBody()) {
            return bodyBuilder.body(response.getBody());
        }

        return bodyBuilder.build();
    }

    private <T> void makeRequest(@Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, getHeader());
        ResponseEntity<Object> statsServerResponse;

        try {
            statsServerResponse = restTemplate.exchange("/hit", HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException ex) {
            ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsByteArray());
            return;
        }

        createResponse(statsServerResponse);
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
