package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.practicum.StatDto;
import ru.practicum.StatOutDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsClient extends BaseClient {

    @Value("${app.name}")
    private String serviceName;

    @Autowired
    public StatisticsClient(@Value("${client.url}") String serviceUrl, @Value("${app.name}") String serviceName, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl + ""))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.serviceName = serviceName;
    }

    public ResponseEntity<Object> postHit(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();

        StatDto statDto = StatDto.builder()
                .app(serviceName)
                .uri(url)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
        return post("/hit", statDto);
    }


    public List<StatOutDto> getStatistics(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("unique", unique);

        if (uris != null && !uris.isEmpty()) {
            params.put("uris", String.join(",", uris));
        }

        ResponseEntity<Object> response = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", params);

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response.getBody(), new TypeReference<List<StatOutDto>>() {
        });
    }
}