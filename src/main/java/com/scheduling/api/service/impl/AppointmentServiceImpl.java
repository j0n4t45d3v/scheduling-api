package com.scheduling.api.service.impl;

import com.scheduling.api.domain.Appointment;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.repositories.AppointmentRepository;
import com.scheduling.api.service.AppointmentService;

public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment confirm(Long id) {
        Appointment appointment = this.getAppointment(id);
        appointment.confirm();
        return this.appointmentRepository.save(appointment);
    }

    @Override
    public Appointment reject(Long id, String rejectReason) {
        Appointment appointment = this.getAppointment(id);
        appointment.reject(rejectReason);
        return this.appointmentRepository.save(appointment);
    }

    @Override
    public Appointment cancel(Long id, String cancelReason) {
        Appointment appointment = this.getAppointment(id);
        appointment.cancel(cancelReason);
        return this.appointmentRepository.save(appointment);
    }

    private Appointment getAppointment(Long id) {
        return this.appointmentRepository.findById(id)
                .orElseThrow( () -> new NotFoundRecordException("appointment not found"));
    }
}
