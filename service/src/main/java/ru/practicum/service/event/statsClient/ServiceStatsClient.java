package ru.practicum.service.event.statsClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitEndpointDto;
import ru.practicum.dto.StatisticDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ServiceStatsClient extends StatsClient {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public ServiceStatsClient(@Value("${STAT-SERVER-URL}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void postStatistic(HttpServletRequest servlet, String app) {
        HitEndpointDto hit = HitEndpointDto
                .builder()
                .app(app)
                .ip(servlet.getRemoteAddr())
                .uri(servlet.getRequestURI())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
        post("/hit", hit);
    }

    public Long getStatistic(Long eventId) {
        String url = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(100).format(formatter),
                "end", LocalDateTime.now().format(formatter),
                "uris", "/events/" + eventId,
                "unique", "false"
        );
        ResponseEntity<List<StatisticDto>> response = get(url, parameters);
        List<StatisticDto> statisticDtoList = response.hasBody() ? response.getBody() : null;
        return statisticDtoList != null && !statisticDtoList.isEmpty() ? statisticDtoList.get(0).getHits() : 0L;
    }
}
