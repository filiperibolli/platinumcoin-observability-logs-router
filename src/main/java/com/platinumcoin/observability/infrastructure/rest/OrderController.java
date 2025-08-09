package com.platinumcoin.observability.infrastructure.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final RestTemplate restTemplate;
    private final String urlBase;

    public OrderController(RestTemplate rt, @Value("${platinumcoin.logs.base.url}") String urlBase) {
        this.restTemplate = rt; this.urlBase = urlBase;
    }

    @GetMapping("/orders/{id}")
    public String getOrder(@PathVariable String id) {
        log.info("Calling platinumcoin-router for order {}", id);
        var result = restTemplate.getForObject(urlBase + "/orders/{id}", String.class, id);
        log.info("Response from platinumcoin-observability-logs for order {} -> {}", id, result);
        return "platinumcoin-router->" + result;
    }
}