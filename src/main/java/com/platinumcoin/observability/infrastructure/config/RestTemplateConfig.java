package com.platinumcoin.observability.infrastructure.config;

import java.time.Duration;
import org.slf4j.MDC;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .additionalInterceptors((req, body, ex) -> {
                    String cid = MDC.get("correlationId");
                    if (cid != null) req.getHeaders().add("x-correlation-id", cid);
                    return ex.execute(req, body);
                })
                .build();
    }
}