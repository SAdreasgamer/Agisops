package com.aegisops.orchestrator.kafka;

import com.aegisops.common.models.AegisEvent;
import com.aegisops.common.models.Event;
import com.aegisops.common.models.Incident;
import com.aegisops.common.models.Tenant;
import com.aegisops.common.repositories.EventRepository;
import com.aegisops.common.repositories.IncidentRepository;
import com.aegisops.common.repositories.TenantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class EventConsumer {

    private final EventRepository eventRepository;
    private final IncidentRepository incidentRepository;
    private final TenantRepository tenantRepository;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    public EventConsumer(
            EventRepository eventRepository,
            IncidentRepository incidentRepository,
            TenantRepository tenantRepository) {
        this.eventRepository = eventRepository;
        this.incidentRepository = incidentRepository;
        this.tenantRepository = tenantRepository;
    }

    @KafkaListener(topics = "aegisops.events.raw", groupId = "orchestrator-group")
    public void consume(AegisEvent aegisEvent) {
        log.info("Received event from Kafka. ID: {}, Source: {}, Type: {}", 
                aegisEvent.getEventId(), aegisEvent.getSource(), aegisEvent.getEventType());

        try {
            // 1. Resolve Tenant
            UUID tenantId;
            try {
                tenantId = UUID.fromString(aegisEvent.getTenantId());
            } catch (IllegalArgumentException e) {
                tenantId = UUID.fromString("00000000-0000-0000-0000-000000000000");
            }

            final UUID finalTenantId = tenantId;
            Tenant tenant = tenantRepository.findById(finalTenantId)
                    .orElseGet(() -> {
                        log.info("Tenant {} not found, creating default/mock tenant entry", finalTenantId);
                        Tenant newTenant = Tenant.builder()
                                .id(finalTenantId)
                                .name("Tenant-" + finalTenantId.toString().substring(0, 8))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();
                        return tenantRepository.save(newTenant);
                    });

            // 2. Parse Event ID
            UUID eventUuid;
            try {
                eventUuid = UUID.fromString(aegisEvent.getEventId());
            } catch (IllegalArgumentException e) {
                eventUuid = UUID.randomUUID();
            }

            // 3. Map and save Event
            String payloadJson = objectMapper.writeValueAsString(aegisEvent.getPayload());
            Event eventEntity = Event.builder()
                    .id(eventUuid)
                    .tenant(tenant)
                    .source(aegisEvent.getSource())
                    .eventType(aegisEvent.getEventType())
                    .payload(payloadJson)
                    .processed(true) // Mark processed as true since we are handling it now
                    .createdAt(LocalDateTime.now())
                    .build();

            eventRepository.save(eventEntity);
            log.info("Saved Event record to DB. UUID: {}", eventUuid);

            // 4. Map and save Incident if event represents an incident trigger
            String source = aegisEvent.getSource();
            String eventType = aegisEvent.getEventType();
            if ("PAGERDUTY".equalsIgnoreCase(source) || "ALERTMANAGER".equalsIgnoreCase(source) 
                    || eventType.contains("incident") || eventType.contains("alert")) {
                
                String title = aegisEvent.getMetadata() != null ? aegisEvent.getMetadata().get("title") : null;
                if (title == null || title.isEmpty()) {
                    title = "Incident triggered by " + source + " (" + eventType + ")";
                }

                String description = aegisEvent.getMetadata() != null ? aegisEvent.getMetadata().get("description") : null;
                if (description == null || description.isEmpty()) {
                    description = payloadJson;
                }

                String incidentSourceId = aegisEvent.getMetadata() != null ? aegisEvent.getMetadata().get("incident_id") : null;
                if (incidentSourceId == null || incidentSourceId.isEmpty()) {
                    incidentSourceId = aegisEvent.getEventId();
                }

                Incident incident = Incident.builder()
                        .id(UUID.randomUUID())
                        .tenant(tenant)
                        .title(title)
                        .description(description)
                        .source(source)
                        .sourceId(incidentSourceId)
                        .status("TRIGGERED")
                        .severity("CRITICAL")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                incidentRepository.save(incident);
                log.info("Saved Incident record to DB for event ID: {}", eventUuid);
            }

        } catch (Exception e) {
            log.error("Failed to process consumed event from Kafka", e);
        }
    }
}
