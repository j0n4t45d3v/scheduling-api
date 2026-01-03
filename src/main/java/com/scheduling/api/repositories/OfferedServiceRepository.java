package com.scheduling.api.repositories;

import com.scheduling.api.domain.OfferedService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferedServiceRepository extends JpaRepository<OfferedService, Long> {
    Optional<OfferedService> findByName(String name);
}
