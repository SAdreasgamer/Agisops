package com.aegisops.ingestion.service;

import com.aegisops.common.models.AegisEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class EventDeduplicationService {

    private final StringRedisTemplate redisTemplate;

    public EventDeduplicationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isDuplicate(AegisEvent event) {
        String key = "dedup:" + event.getDeduplicationKey();
        // Set key with value "1" if it does not exist, with a 1-hour expiration
        Boolean wasSet = redisTemplate.opsForValue()
                .setIfAbsent(key, "1", Duration.ofHours(1));
        
        // If it was not set, it means the key already existed, hence it's a duplicate
        return Boolean.FALSE.equals(wasSet);
    }
}
