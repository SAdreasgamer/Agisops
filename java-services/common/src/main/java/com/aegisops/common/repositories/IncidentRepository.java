package com.aegisops.common.repositories;

import com.aegisops.common.models.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    List<Incident> findByTenantId(UUID tenantId);
    List<Incident> findByTenantIdAndStatus(UUID tenantId, String status);
}
