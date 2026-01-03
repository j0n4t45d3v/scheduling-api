package com.scheduling.api.service.impl;

import com.scheduling.api.domain.OfferedService;
import com.scheduling.api.infra.errors.bussines.ConflictRecordException;
import com.scheduling.api.repositories.OfferedServiceRepository;
import com.scheduling.api.repositories.ServiceScheduleRepository;
import com.scheduling.api.repositories.ServiceWorkDayRepository;
import com.scheduling.api.service.OfferedServiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OfferedServiceServiceImpl implements OfferedServiceService {

    private final OfferedServiceRepository offeredServiceRepository;
    private final ServiceScheduleRepository serviceScheduleRepository;
    private final ServiceWorkDayRepository serviceWorkDayRepository;

    public OfferedServiceServiceImpl(
            OfferedServiceRepository offeredServiceRepository,
            ServiceScheduleRepository serviceScheduleRepository,
            ServiceWorkDayRepository serviceWorkDayRepository
    ) {
        this.offeredServiceRepository = offeredServiceRepository;
        this.serviceScheduleRepository = serviceScheduleRepository;
        this.serviceWorkDayRepository = serviceWorkDayRepository;
    }

    @Override
    @Transactional
    public OfferedService create(OfferedService offeredService) {
        this.offeredServiceRepository.findByName(offeredService.getName())
                .ifPresent(offeredServiceFound -> {
                    throw new ConflictRecordException("Already exists service with name: " + offeredServiceFound.getName());
                });
        OfferedService offeredSaved = this.offeredServiceRepository.save(offeredService);
        this.serviceWorkDayRepository.saveAll(offeredService.getWorkDays());
        this.serviceScheduleRepository.saveAll(offeredService.getSchedules());
        return offeredSaved;
    }
}
