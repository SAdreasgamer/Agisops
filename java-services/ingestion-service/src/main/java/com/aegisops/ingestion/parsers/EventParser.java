package com.aegisops.ingestion.parsers;

import com.aegisops.common.models.AegisEvent;
import java.util.Map;

public interface EventParser {
    AegisEvent parse(Map<String, Object> payload);
    String getSource();
}
