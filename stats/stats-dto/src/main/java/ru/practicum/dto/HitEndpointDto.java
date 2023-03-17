package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import ru.practicum.config.Administrator;
import ru.practicum.config.Client;
import ru.practicum.config.Creation;
import ru.practicum.config.DateValidator;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class HitEndpointDto {
    static final String IPV4_REGEX =
            "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";
    private Long id;
    @JsonView({Client.class, Administrator.class})
    @NotEmpty(groups = {Creation.class}, message = "неправильный APP")
    @NotNull(groups = {Creation.class}, message = "APP не может быть NULL")
    private String app;

    @JsonView({Client.class, Administrator.class})
    @URL(groups = {Creation.class}, protocol = "http", message = "неправильный URI")
    @NotNull(groups = {Creation.class}, message = "URI не может быть NULL")
    private String uri;

    @JsonView({Client.class, Administrator.class})
    @Pattern(groups = {Creation.class}, regexp = IPV4_REGEX, message = "неправильный IP")
    @NotNull(groups = {Creation.class}, message = "IP не может быть NULL")
    private String ip;

    @NotNull(groups = {Creation.class}, message = "timestamp не может быть NULL")
    @DateValidator
    private String timestamp;
}
