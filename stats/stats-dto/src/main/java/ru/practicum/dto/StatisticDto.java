package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.config.Administrator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto {
    @JsonProperty("app")
    @JsonView({Administrator.class})
    private String app;
    @JsonProperty("uri")
    @JsonView({Administrator.class})
    private String uri;
    @JsonProperty("hits")
    @JsonView({Administrator.class})
    private Long hits;
}
