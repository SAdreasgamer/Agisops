package com.aegisops.orchestrator;

import com.aegisops.common.models.AegisEvent;
import com.aegisops.common.models.Event;
import com.aegisops.common.models.Incident;
import com.aegisops.common.models.Tenant;
import com.aegisops.common.repositories.EventRepository;
import com.aegisops.common.repositories.IncidentRepository;
import com.aegisops.common.repositories.TenantRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class E2EFlowIntegrationTest {

    @Autowired
    private KafkaTemplate<String, AegisEvent> kafkaTemplate;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private TenantRepository tenantRepository;

    private static final String DEFAULT_TENANT_ID = "00000000-0000-0000-0000-000000000000";

    @BeforeEach
    public void checkInfrastructure() {
        boolean dbAlive = isServiceAvailable("localhost", 5432);
        boolean kafkaAlive = isServiceAvailable("localhost", 9092);
        
        Assumptions.assumeTrue(dbAlive && kafkaAlive, 
                "Infrastructure services (Postgres on 5432, Kafka on 9092) are not fully running. Skipping E2E Integration Test.");
    }

    private boolean isServiceAvailable(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Test
    public void testFullE2EFlowFromKafkaToDatabase() {
        String eventId = UUID.randomUUID().toString();
        log("Generated test Event ID: " + eventId);

        // 1. Prepare AegisEvent webhook payload simulation
        Map<String, Object> innerPayload = new HashMap<>();
        innerPayload.put("incident_key", "alert-e2e-999");
        innerPayload.put("description", "E2E database high temperature threshold alert");

        Map<String, String> metadata = new HashMap<>();
        metadata.put("title", "High Temperature in Database Rack A");
        metadata.put("description", "Sensor reported rack temperature above 85C");
        metadata.put("incident_id", "alert-e2e-999");

        AegisEvent event = AegisEvent.builder()
                .eventId(eventId)
                .eventType("incident.alert")
                .timestamp(LocalDateTime.now())
                .source("ALERTMANAGER")
                .tenantId(DEFAULT_TENANT_ID)
                .payload(innerPayload)
                .metadata(metadata)
                .build();

        // 2. Publish simulation event to Kafka
        log("Publishing test AegisEvent to Kafka topic...");
        kafkaTemplate.send("aegisops.events.raw", event.getDeduplicationKey(), event);

        // 3. Wait asynchronously for consumer to persist it
        log("Waiting for consumer to process and save Event and Incident records to DB...");
        UUID eventUuid = UUID.fromString(eventId);
        
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    // Check Event table
                    assertTrue(eventRepository.existsById(eventUuid), "Event record should be saved in DB");
                    
                    // Check Incident table
                    List<Incident> incidents = incidentRepository.findAll();
                    boolean foundMappedIncident = incidents.stream()
                            .anyMatch(inc -> "alert-e2e-999".equals(inc.getSourceId()) && "ALERTMANAGER".equals(inc.getSource()));
                    assertTrue(foundMappedIncident, "Mapped Incident record should be saved in DB");
                });

        // 4. Detailed Assertions
        Event savedEvent = eventRepository.findById(eventUuid).orElseThrow();
        assertEquals("ALERTMANAGER", savedEvent.getSource());
        assertEquals("incident.alert", savedEvent.getEventType());
        assertTrue(savedEvent.isProcessed());
        assertNotNull(savedEvent.getPayload());

        Incident savedIncident = incidentRepository.findAll().stream()
                .filter(inc -> "alert-e2e-999".equals(inc.getSourceId()) && "ALERTMANAGER".equals(inc.getSource()))
                .findFirst()
                .orElseThrow();
        assertEquals(DEFAULT_TENANT_ID, savedIncident.getTenant().getId().toString());
        assertEquals("High Temperature in Database Rack A", savedIncident.getTitle());
        assertEquals("TRIGGERED", savedIncident.getStatus());
        assertEquals("CRITICAL", savedIncident.getSeverity());

        log("Successfully verified E2E flow! Event and Incident persisted and verified.");
    }

    private void log(String msg) {
        System.out.println(">>> [E2E-TEST] " + msg);
    }
}
