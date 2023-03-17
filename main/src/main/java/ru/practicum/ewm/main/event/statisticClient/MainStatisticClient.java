package ru.practicum.ewm.main.event.statisticClient;

import dto.HitEndpointDto;
import dto.StatisticDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.StatisticClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MainStatisticClient extends StatisticClient {

    @Autowired
    public MainStatisticClient(@Value("${STAT-SERVER-URL}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void add(HttpServletRequest servlet, String app) {
        HitEndpointDto hitEndpointDto = new HitEndpointDto(null, app, servlet.getRequestURI(),
                servlet.getRemoteAddr(), LocalDateTime.now());
        addHit(hitEndpointDto);
    }

    public Long get(Long eventId) {
        String url = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(100),
                "end", LocalDateTime.now(),
                "uris", "/events/" + eventId,
                "unique", "false"
        );
        ResponseEntity<List<StatisticDto>> response = getStatistic(url, parameters);
        List<StatisticDto> viewStatsList = response.hasBody() ? response.getBody() : null;

        return viewStatsList != null && !viewStatsList.isEmpty() ? viewStatsList.get(0).getHits() : 0L;
    }
}
