package com.aegisops.common.repositories;

import com.aegisops.common.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByTenantId(UUID tenantId);
    List<Event> findByProcessed(boolean processed);
}
