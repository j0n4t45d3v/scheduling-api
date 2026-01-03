package com.scheduling.api.repositories;

import com.scheduling.api.domain.ServiceSchedules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceScheduleRepository extends JpaRepository<ServiceSchedules, Long> {
}
