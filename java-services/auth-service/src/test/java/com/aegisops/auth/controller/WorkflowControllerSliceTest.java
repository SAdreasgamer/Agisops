package com.aegisops.auth.controller;

import com.aegisops.auth.config.AuthSecurityConfig;
import com.aegisops.common.models.Tenant;
import com.aegisops.common.models.Workflow;
import com.aegisops.common.repositories.TenantRepository;
import com.aegisops.common.repositories.WorkflowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WorkflowController.class)
@Import(AuthSecurityConfig.class)
public class WorkflowControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkflowRepository workflowRepository;

    @MockitoBean
    private TenantRepository tenantRepository;

    @Test
    public void testGetAllWorkflowsWithDevRole() throws Exception {
        when(workflowRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/workflows")
                .header("X-User-Id", "dev-user-123")
                .header("X-User-Roles", "DEV")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllWorkflowsUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/workflows")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()); // Spring Security default without Authentication
    }

    @Test
    public void testCreateWorkflowWithSreRole() throws Exception {
        Tenant mockTenant = Tenant.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .name("Default Tenant")
                .build();
        Workflow mockWorkflow = Workflow.builder()
                .id(UUID.randomUUID())
                .name("CI Pipeline")
                .status("ACTIVE")
                .tenant(mockTenant)
                .build();

        when(tenantRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockTenant));
        when(workflowRepository.save(any(Workflow.class))).thenReturn(mockWorkflow);

        String payload = "{\"name\": \"CI Pipeline\", \"status\": \"ACTIVE\"}";

        mockMvc.perform(post("/api/v1/workflows")
                .header("X-User-Id", "sre-user-123")
                .header("X-User-Roles", "SRE")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateWorkflowWithDevRoleForbidden() throws Exception {
        String payload = "{\"name\": \"CI Pipeline\", \"status\": \"ACTIVE\"}";

        mockMvc.perform(post("/api/v1/workflows")
                .header("X-User-Id", "dev-user-123")
                .header("X-User-Roles", "DEV")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteWorkflowWithAdminRole() throws Exception {
        UUID id = UUID.randomUUID();
        when(workflowRepository.existsById(id)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/workflows/" + id)
                .header("X-User-Id", "admin-user-123")
                .header("X-User-Roles", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteWorkflowWithSreRoleForbidden() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/workflows/" + id)
                .header("X-User-Id", "sre-user-123")
                .header("X-User-Roles", "SRE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
