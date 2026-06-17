package com.aegisops.ingestion.parsers;

import com.aegisops.common.models.AegisEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class GitHubEventParser implements EventParser {

    @SuppressWarnings("unchecked")
    @Override
    public AegisEvent parse(Map<String, Object> payload) {
        String eventId = UUID.randomUUID().toString();
        String eventType = "github.event";
        Map<String, String> metadata = new HashMap<>();

        if (payload.containsKey("workflow_run")) {
            Map<String, Object> workflowRun = (Map<String, Object>) payload.get("workflow_run");
            eventId = String.valueOf(workflowRun.getOrDefault("id", eventId));
            
            String status = String.valueOf(workflowRun.getOrDefault("status", ""));
            String conclusion = String.valueOf(workflowRun.getOrDefault("conclusion", ""));
            
            if ("completed".equalsIgnoreCase(status) && "failure".equalsIgnoreCase(conclusion)) {
                eventType = "ci.build.failed";
            } else {
                eventType = "github.workflow.run";
            }

            metadata.put("status", status);
            metadata.put("conclusion", conclusion);
            metadata.put("html_url", String.valueOf(workflowRun.getOrDefault("html_url", "")));
            metadata.put("branch", String.valueOf(workflowRun.getOrDefault("head_branch", "")));
            metadata.put("commit_sha", String.valueOf(workflowRun.getOrDefault("head_sha", "")));
        } else if (payload.containsKey("zen")) {
            eventType = "github.ping";
            metadata.put("zen", String.valueOf(payload.get("zen")));
        }

        if (payload.containsKey("repository")) {
            Map<String, Object> repo = (Map<String, Object>) payload.get("repository");
            metadata.put("repository", String.valueOf(repo.getOrDefault("full_name", "")));
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
        return "GITHUB";
    }
}
