package com.scheduling.api.repositories;

import com.scheduling.api.domain.OfferedService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferedServiceRepository extends JpaRepository<OfferedService, Long> {
}
