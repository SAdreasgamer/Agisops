package com.aegisops.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class AegisEvent {
    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private String source;
    private String tenantId;
    private Map<String, Object> payload;
    private Map<String, String> metadata;

    @JsonIgnore
    public String getDeduplicationKey() {
        return source + ":" + eventType + ":" + eventId;
    }
}
