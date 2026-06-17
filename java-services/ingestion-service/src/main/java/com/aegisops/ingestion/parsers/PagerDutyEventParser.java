package com.aegisops.ingestion.parsers;

import com.aegisops.common.models.AegisEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class PagerDutyEventParser implements EventParser {

    @SuppressWarnings("unchecked")
    @Override
    public AegisEvent parse(Map<String, Object> payload) {
        String eventId = UUID.randomUUID().toString();
        String eventType = "pagerduty.event";
        Map<String, String> metadata = new HashMap<>();

        if (payload.containsKey("event")) {
            Map<String, Object> event = (Map<String, Object>) payload.get("event");
            eventId = String.valueOf(event.getOrDefault("id", eventId));
            String rawType = String.valueOf(event.getOrDefault("event_type", ""));
            
            if ("incident.triggered".equalsIgnoreCase(rawType)) {
                eventType = "incident.created";
            } else {
                eventType = "pagerduty." + rawType;
            }

            if (event.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) event.get("data");
                metadata.put("incident_id", String.valueOf(data.getOrDefault("id", "")));
                metadata.put("title", String.valueOf(data.getOrDefault("title", "")));
                
                if (data.containsKey("service")) {
                    metadata.put("service", String.valueOf(data.get("service")));
                }
            }
        }

        return AegisEvent.builder()
                .eventId(eventId)
                .eventType(eventType)
                .timestamp(LocalDateTime.now())
                .source(getSource())
                .tenantId("00000000-0000-0000-0000-000000000000")
                .payload(payload)
                .metadata(metadata)
                .build();
    }

    @Override
    public String getSource() {
        return "PAGERDUTY";
    }
}
