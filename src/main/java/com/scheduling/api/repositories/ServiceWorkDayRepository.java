package com.scheduling.api.repositories;

import com.scheduling.api.domain.ServiceSchedules;
import com.scheduling.api.domain.ServiceWorkDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceWorkDayRepository extends JpaRepository<ServiceWorkDay, Long> {
}
