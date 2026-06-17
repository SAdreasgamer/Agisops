-- Seed default tenant
INSERT INTO tenants (id, name, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-000000000000', 'Default Tenant', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Seed a default workflow template
INSERT INTO workflows (id, tenant_id, name, status, created_at, updated_at)
VALUES ('11111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000000', 'Incident Investigation Workflow', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Seed workflow steps
INSERT INTO workflow_steps (id, workflow_id, name, status, step_order, input_data, output_data, error_message, started_at, completed_at)
VALUES 
('11111111-1111-1111-1111-222222222221', '11111111-1111-1111-1111-111111111111', 'planning', 'PENDING', 1, NULL, NULL, NULL, NULL, NULL),
('11111111-1111-1111-1111-222222222222', '11111111-1111-1111-1111-111111111111', 'retrieval', 'PENDING', 2, NULL, NULL, NULL, NULL, NULL),
('11111111-1111-1111-1111-222222222223', '11111111-1111-1111-1111-111111111111', 'agent_execution', 'PENDING', 3, NULL, NULL, NULL, NULL, NULL),
('11111111-1111-1111-1111-222222222224', '11111111-1111-1111-1111-111111111111', 'verification', 'PENDING', 4, NULL, NULL, NULL, NULL, NULL);

-- Seed initial critical incident
INSERT INTO incidents (id, tenant_id, title, description, source, source_id, status, severity, created_at, updated_at)
VALUES ('22222222-2222-2222-2222-222222222222', '00000000-0000-0000-0000-000000000000', 'Database CPU Spike Alert', 'CPU utilization on primary PostgreSQL db exceeds 90%', 'ALERTMANAGER', 'alert-db-cpu-999', 'TRIGGERED', 'CRITICAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
