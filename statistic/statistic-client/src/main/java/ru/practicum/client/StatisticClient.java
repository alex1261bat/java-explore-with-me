package ru.practicum.client;

import dto.HitEndpointDto;
import dto.StatisticDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatisticClient {
    private final RestTemplate restTemplate;

    @Autowired
    public StatisticClient(@Value("${stat-server.url}") String url,
                           RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .build();
    }

    public void addHit(HttpServletRequest httpServletRequest) {
        HitEndpointDto hitEndpointDto = new HitEndpointDto(
                null,
                "main",
                httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now()
        );

        restTemplate.postForEntity("/hit",
                new HttpEntity<>(hitEndpointDto),
                HitEndpointDto.class);
    }

    public ResponseEntity<List<StatisticDto>> getStatistic(String start, String end, List<String> uris, boolean unique) {
        return restTemplate.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET,
                getHttpEntity(null),
                new ParameterizedTypeReference<>() {
                },
                start, end, uris, unique);
    }

    private <T> HttpEntity<T> getHttpEntity(T dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return dto == null ? new HttpEntity<>(headers) : new HttpEntity<>(dto, headers);
    }
}
