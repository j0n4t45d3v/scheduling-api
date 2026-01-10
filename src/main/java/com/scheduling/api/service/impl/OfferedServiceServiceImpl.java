package com.scheduling.api.service.impl;

import com.scheduling.api.domain.OfferedService;
import com.scheduling.api.infra.errors.bussines.ConflictRecordException;
import com.scheduling.api.repositories.OfferedServiceRepository;
import com.scheduling.api.service.OfferedServiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OfferedServiceServiceImpl implements OfferedServiceService {

    private final OfferedServiceRepository offeredServiceRepository;

    public OfferedServiceServiceImpl(
            OfferedServiceRepository offeredServiceRepository
    ) {
        this.offeredServiceRepository = offeredServiceRepository;
    }

    @Override
    @Transactional
    public OfferedService create(OfferedService offeredService) {
        this.offeredServiceRepository.findByName(offeredService.getName())
                .ifPresent(offeredServiceFound -> {
                    throw new ConflictRecordException("Already exists service with name: " + offeredServiceFound.getName());
                });
        return this.offeredServiceRepository.save(offeredService);
    }
}
