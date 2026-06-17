package com.aegisops.ingestion;

import com.aegisops.ingestion.controller.WebhookController;
import com.aegisops.ingestion.service.WebhookSignatureVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = IngestionApplication.class)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"aegisops.events.raw"})
@ActiveProfiles("test")
public class WebhookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StringRedisTemplate redisTemplate;

    @MockitoBean
    private ValueOperations<String, String> valueOperations;

    @MockitoBean
    private WebhookSignatureVerifier signatureVerifier;

    @BeforeEach
    public void setup() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testGitHubWebhookUnauthorizedWithoutSignature() throws Exception {
        String payload = "{\"zen\": \"Keep it simple, stupid.\"}";
        when(signatureVerifier.isValidGitHubSignature(any(), any())).thenReturn(false);

        mockMvc.perform(post("/api/v1/ingest/github")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGitHubWebhookSuccess() throws Exception {
        String payload = "{\"zen\": \"Keep it simple, stupid.\"}";
        when(signatureVerifier.isValidGitHubSignature(any(), any())).thenReturn(true);
        when(valueOperations.setIfAbsent(any(String.class), any(String.class), any(Duration.class))).thenReturn(true); // Not duplicate

        mockMvc.perform(post("/api/v1/ingest/github")
                .header("X-Hub-Signature-256", "sha256=mocksignature")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    public void testGitHubWebhookDuplicate() throws Exception {
        String payload = "{\"zen\": \"Keep it simple, stupid.\"}";
        when(signatureVerifier.isValidGitHubSignature(any(), any())).thenReturn(true);
        when(valueOperations.setIfAbsent(any(String.class), any(String.class), any(Duration.class))).thenReturn(false); // Duplicate

        mockMvc.perform(post("/api/v1/ingest/github")
                .header("X-Hub-Signature-256", "sha256=mocksignature")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DUPLICATE"));
    }

    @Test
    public void testPagerDutyWebhookSuccess() throws Exception {
        String payload = "{\"event\": {\"id\": \"pd-123\", \"event_type\": \"incident.triggered\", \"data\": {\"id\": \"inc-123\", \"title\": \"DB fail\"}}}";
        when(valueOperations.setIfAbsent(any(String.class), any(String.class), any(Duration.class))).thenReturn(true);

        mockMvc.perform(post("/api/v1/ingest/pagerduty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    public void testAlertManagerWebhookSuccess() throws Exception {
        String payload = "{\"status\": \"firing\", \"alerts\": [{\"labels\": {\"alertname\": \"latency_spike\", \"severity\": \"critical\"}}]}";
        when(valueOperations.setIfAbsent(any(String.class), any(String.class), any(Duration.class))).thenReturn(true);

        mockMvc.perform(post("/api/v1/ingest/alertmanager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }
}
