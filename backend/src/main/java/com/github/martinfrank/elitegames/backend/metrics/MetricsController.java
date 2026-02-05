package com.github.martinfrank.elitegames.backend.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsController.class);

    @GetMapping("/health")
    public ResponseEntity<Void> getHealthCheck() {
        LOGGER.info("GET /health");
        return ResponseEntity.ok().build();
    }
}
