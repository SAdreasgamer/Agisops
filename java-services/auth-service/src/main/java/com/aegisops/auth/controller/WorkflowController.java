package com.aegisops.auth.controller;

import com.aegisops.common.models.Tenant;
import com.aegisops.common.models.Workflow;
import com.aegisops.common.repositories.TenantRepository;
import com.aegisops.common.repositories.WorkflowRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workflows")
public class WorkflowController {

    private static final UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final WorkflowRepository workflowRepository;
    private final TenantRepository tenantRepository;

    public WorkflowController(WorkflowRepository workflowRepository, TenantRepository tenantRepository) {
        this.workflowRepository = workflowRepository;
        this.tenantRepository = tenantRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DEV', 'SRE', 'ADMIN')")
    public ResponseEntity<List<Workflow>> getAllWorkflows() {
        return ResponseEntity.ok(workflowRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DEV', 'SRE', 'ADMIN')")
    public ResponseEntity<Workflow> getWorkflowById(@PathVariable UUID id) {
        return workflowRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SRE', 'ADMIN')")
    public ResponseEntity<Workflow> createWorkflow(@RequestBody WorkflowRequest request) {
        Tenant tenant = tenantRepository.findById(DEFAULT_TENANT_ID)
                .orElseGet(() -> tenantRepository.save(
                        Tenant.builder()
                                .id(DEFAULT_TENANT_ID)
                                .name("Default Tenant")
                                .build()
                ));

        Workflow workflow = Workflow.builder()
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .tenant(tenant)
                .build();

        Workflow savedWorkflow = workflowRepository.save(workflow);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWorkflow);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SRE', 'ADMIN')")
    public ResponseEntity<Workflow> updateWorkflow(@PathVariable UUID id, @RequestBody WorkflowRequest request) {
        return workflowRepository.findById(id)
                .map(workflow -> {
                    workflow.setName(request.getName());
                    if (request.getStatus() != null) {
                        workflow.setStatus(request.getStatus());
                    }
                    return ResponseEntity.ok(workflowRepository.save(workflow));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable UUID id) {
        if (workflowRepository.existsById(id)) {
            workflowRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Static request DTO
    public static class WorkflowRequest {
        private String name;
        private String status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
