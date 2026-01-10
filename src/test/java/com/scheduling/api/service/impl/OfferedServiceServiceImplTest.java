package com.scheduling.api.service.impl;

import com.scheduling.api.domain.OfferedService;
import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.infra.errors.bussines.ConflictRecordException;
import com.scheduling.api.repositories.OfferedServiceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferedServiceServiceImplTest {

    @Mock
    private OfferedServiceRepository repository;

    @InjectMocks
    private OfferedServiceServiceImpl offeredServiceService;

    @Test
    @DisplayName("should create a valid service when it not exists")
    void shouldCreateAValidService() {
        var offeredService = OfferedService.builder()
                .setName("name")
                .setDescription("description")
                .addWorkDay(WeekDays.SUNDAY)
                .addSchedule(new Schedule(LocalTime.now()))
                .build();

        when(this.repository.findByName(anyString())).thenReturn(Optional.empty());

        this.offeredServiceService.create(offeredService);

        verify(this.repository, times(1)).findByName("name");
        verify(this.repository, times(1)).save(same(offeredService));
    }

    @Test
    @DisplayName("should throw conflict record in create service when already exists a service with same name")
    void shouldThrowConflictRecordInCreateServiceWhenAlreadyExistsAServiceWithSameName() {
        var offeredService = OfferedService.builder()
                .setName("name")
                .setDescription("description")
                .addWorkDay(WeekDays.SUNDAY)
                .addSchedule(new Schedule(LocalTime.now()))
                .build();

        when(this.repository.findByName(anyString())).thenReturn(Optional.of(offeredService));

        assertThrows(ConflictRecordException.class, () -> this.offeredServiceService.create(offeredService));

        verify(this.repository, times(1)).findByName("name");
        verify(this.repository, never()).save(same(offeredService));
    }


}