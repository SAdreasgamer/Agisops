package com.aegisops.ingestion.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventAck {
    private String eventId;
    private String status;

    public static EventAck accepted(String eventId) {
        return new EventAck(eventId, "ACCEPTED");
    }

    public static EventAck duplicate(String eventId) {
        return new EventAck(eventId, "DUPLICATE");
    }
}
