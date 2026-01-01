package com.scheduling.api.service.impl;

import com.scheduling.api.domain.Appointment;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.repositories.AppointmentRepository;
import com.scheduling.api.service.AppointmentService;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void confirm(Long id) {
        Appointment appointment = this.getAppointment(id);
        appointment.confirm();
        this.appointmentRepository.save(appointment);
    }

    @Override
    public void reject(Long id, String rejectReason) {
        Appointment appointment = this.getAppointment(id);
        appointment.reject(rejectReason);
        this.appointmentRepository.save(appointment);
    }

    @Override
    public void cancel(Long id, String cancelReason) {
        Appointment appointment = this.getAppointment(id);
        appointment.cancel(cancelReason);
        this.appointmentRepository.save(appointment);
    }

    private Appointment getAppointment(Long id) {
        return this.appointmentRepository.findById(id)
                .orElseThrow( () -> new NotFoundRecordException("appointment not found"));
    }
}
