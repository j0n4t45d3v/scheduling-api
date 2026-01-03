package com.scheduling.api.service.impl;

import com.scheduling.api.domain.Appointment;
import com.scheduling.api.domain.OfferedService;
import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.infra.providers.ClockProvider;
import com.scheduling.api.repositories.AppointmentRepository;
import com.scheduling.api.repositories.OfferedServiceRepository;
import com.scheduling.api.service.SchedulingService;

@org.springframework.stereotype.Service
public class SchedulingServiceImpl implements SchedulingService {

    private final OfferedServiceRepository offeredServiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final ClockProvider clockProvider;

    public SchedulingServiceImpl(
            OfferedServiceRepository offeredServiceRepository,
            AppointmentRepository appointmentRepository,
            ClockProvider clockProvider
    ) {
        this.offeredServiceRepository = offeredServiceRepository;
        this.appointmentRepository = appointmentRepository;
        this.clockProvider = clockProvider;
    }

    @Override
    public Appointment schedule(Long serviceId, DayHour appointment) {
        OfferedService offeredService = this.offeredServiceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundRecordException("service not found"));
        Appointment doneAppointment = offeredService.schedule(appointment, DayHour.now(this.clockProvider));
        this.appointmentRepository.save(doneAppointment);
        return doneAppointment;
    }
}
