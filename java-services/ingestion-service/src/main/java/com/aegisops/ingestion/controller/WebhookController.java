package com.aegisops.ingestion.controller;

import com.aegisops.common.models.AegisEvent;
import com.aegisops.ingestion.parsers.AlertManagerEventParser;
import com.aegisops.ingestion.parsers.GitHubEventParser;
import com.aegisops.ingestion.parsers.PagerDutyEventParser;
import com.aegisops.ingestion.service.EventDeduplicationService;
import com.aegisops.ingestion.service.WebhookSignatureVerifier;
import com.aegisops.ingestion.kafka.EventPublisher;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ingest")
@Slf4j
public class WebhookController {

    private final GitHubEventParser githubEventParser;
    private final PagerDutyEventParser pagerDutyEventParser;
    private final AlertManagerEventParser alertManagerEventParser;
    private final EventDeduplicationService deduplicationService;
    private final WebhookSignatureVerifier signatureVerifier;
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    public WebhookController(
            GitHubEventParser githubEventParser,
            PagerDutyEventParser pagerDutyEventParser,
            AlertManagerEventParser alertManagerEventParser,
            EventDeduplicationService deduplicationService,
            WebhookSignatureVerifier signatureVerifier,
            EventPublisher eventPublisher,
            ObjectMapper objectMapper) {
        this.githubEventParser = githubEventParser;
        this.pagerDutyEventParser = pagerDutyEventParser;
        this.alertManagerEventParser = alertManagerEventParser;
        this.deduplicationService = deduplicationService;
        this.signatureVerifier = signatureVerifier;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/github")
    public ResponseEntity<EventAck> handleGitHubWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature) {
        
        log.info("Received GitHub webhook event. Signature present: {}", signature != null);

        // 1. Verify webhook signature
        if (!signatureVerifier.isValidGitHubSignature(payload, signature)) {
            log.warn("Invalid signature on GitHub webhook call");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // 2. Parse payload
            Map<String, Object> mapPayload = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            AegisEvent event = githubEventParser.parse(mapPayload);

            // 3. Deduplicate
            if (deduplicationService.isDuplicate(event)) {
                log.info("GitHub event {} is a duplicate, skipping", event.getEventId());
                return ResponseEntity.ok(EventAck.duplicate(event.getEventId()));
            }

            // 4. Publish to Kafka
            eventPublisher.publish(event);
            return ResponseEntity.accepted().body(EventAck.accepted(event.getEventId()));

        } catch (JacksonException e) {
            log.error("Failed to parse GitHub payload", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/pagerduty")
    public ResponseEntity<EventAck> handlePagerDutyWebhook(@RequestBody String payload) {
        log.info("Received PagerDuty webhook event");
        try {
            Map<String, Object> mapPayload = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            AegisEvent event = pagerDutyEventParser.parse(mapPayload);

            if (deduplicationService.isDuplicate(event)) {
                log.info("PagerDuty event {} is a duplicate, skipping", event.getEventId());
                return ResponseEntity.ok(EventAck.duplicate(event.getEventId()));
            }

            eventPublisher.publish(event);
            return ResponseEntity.accepted().body(EventAck.accepted(event.getEventId()));
        } catch (JacksonException e) {
            log.error("Failed to parse PagerDuty payload", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/alertmanager")
    public ResponseEntity<EventAck> handleAlertManagerWebhook(@RequestBody String payload) {
        log.info("Received AlertManager webhook event");
        try {
            Map<String, Object> mapPayload = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            AegisEvent event = alertManagerEventParser.parse(mapPayload);

            if (deduplicationService.isDuplicate(event)) {
                log.info("AlertManager event {} is a duplicate, skipping", event.getEventId());
                return ResponseEntity.ok(EventAck.duplicate(event.getEventId()));
            }

            eventPublisher.publish(event);
            return ResponseEntity.accepted().body(EventAck.accepted(event.getEventId()));
        } catch (JacksonException e) {
            log.error("Failed to parse AlertManager payload", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
