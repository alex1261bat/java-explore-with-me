package ru.practicum.ewm.main.event.statisticClient;

import dto.StatisticDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatisticClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MainStatisticClient extends StatisticClient {

    public MainStatisticClient(@Value("${STAT-SERVER-URL}") String url, RestTemplateBuilder restTemplateBuilder) {
        super(url, restTemplateBuilder);
    }

    public void post(HttpServletRequest httpServletRequest) {
        addHit(httpServletRequest);
    }

    public Long getStatistic(Long eventId) {
        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(100),
                "end", LocalDateTime.now(),
                "uris", "/events/" + eventId,
                "unique", "false"
        );
        ResponseEntity<List<StatisticDto>> response = getStatistic(parameters.get("start").toString(),
                parameters.get("end").toString(),
                List.of(parameters.get("uris").toString()), false);
        List<StatisticDto> statisticDtoList = response.hasBody() ? response.getBody() : null;

        return statisticDtoList != null && !statisticDtoList.isEmpty() ? statisticDtoList.get(0).getHits() : 0L;
    }
}
