package com.aegisops.ingestion.kafka;

import com.aegisops.common.models.AegisEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    private static final String TOPIC = "aegisops.events.raw";
    private final KafkaTemplate<String, AegisEvent> kafkaTemplate;

    public EventPublisher(KafkaTemplate<String, AegisEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(AegisEvent event) {
        kafkaTemplate.send(TOPIC, event.getEventId(), event);
    }
}
