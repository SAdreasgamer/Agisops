package com.aegisops.ingestion.parsers;

import com.aegisops.common.models.AegisEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class AlertManagerEventParser implements EventParser {

    @SuppressWarnings("unchecked")
    @Override
    public AegisEvent parse(Map<String, Object> payload) {
        String eventId = UUID.randomUUID().toString();
        String eventType = "metric.latency.spike";
        Map<String, String> metadata = new HashMap<>();

        String status = String.valueOf(payload.getOrDefault("status", "firing"));
        metadata.put("status", status);

        if (payload.containsKey("alerts")) {
            List<Map<String, Object>> alerts = (List<Map<String, Object>>) payload.get("alerts");
            if (!alerts.isEmpty()) {
                Map<String, Object> firstAlert = alerts.get(0);
                
                if (firstAlert.containsKey("labels")) {
                    Map<String, Object> labels = (Map<String, Object>) firstAlert.get("labels");
                    String alertName = String.valueOf(labels.getOrDefault("alertname", "alert"));
                    String severity = String.valueOf(labels.getOrDefault("severity", "warning"));
                    
                    if (alertName.toLowerCase().contains("latency") || alertName.toLowerCase().contains("delay")) {
                        eventType = "metric.latency.spike";
                    } else if (alertName.toLowerCase().contains("oom") || alertName.toLowerCase().contains("out-of-memory")) {
                        eventType = "k8s.pod.oom";
                    } else if (alertName.toLowerCase().contains("crash") || alertName.toLowerCase().contains("crashloop")) {
                        eventType = "k8s.pod.crashloop";
                    } else {
                        eventType = "alertmanager." + alertName;
                    }
                    
                    metadata.put("alertname", alertName);
                    metadata.put("severity", severity);
                }

                if (firstAlert.containsKey("annotations")) {
                    Map<String, Object> annotations = (Map<String, Object>) firstAlert.get("annotations");
                    metadata.put("description", String.valueOf(annotations.getOrDefault("description", "")));
                    metadata.put("summary", String.valueOf(annotations.getOrDefault("summary", "")));
                }

                metadata.put("startsAt", String.valueOf(firstAlert.getOrDefault("startsAt", "")));
                metadata.put("generatorURL", String.valueOf(firstAlert.getOrDefault("generatorURL", "")));
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
        return "ALERTMANAGER";
    }
}
