package com.aegisops.common.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AegisEvent {
    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private String source;
    private String tenantId;
    private Map<String, Object> payload;
    private Map<String, String> metadata;

    public String getDeduplicationKey() {
        return source + ":" + eventType + ":" + eventId;
    }
}
