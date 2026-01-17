package com.scheduling.api.controller;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.dto.appointment.Reason;
import com.scheduling.api.dto.appointment.RescheduleBody;
import com.scheduling.api.dto.appointment.ScheduleBody;
import com.scheduling.api.service.AppointmentService;
import com.scheduling.api.service.SchedulingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final SchedulingService schedulingService;

    public AppointmentController(
            AppointmentService appointmentService,
           SchedulingService schedulingService
    ) {
        this.appointmentService = appointmentService;
        this.schedulingService = schedulingService;
    }

    @PostMapping
    public ResponseEntity<Void> schedule(@RequestBody ScheduleBody body) {
        var appointment = this.schedulingService.schedule(body.service(), new DayHour(body.date(), body.time()));
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(appointment.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .build();
    }

    @PostMapping("/reschedule")
    public ResponseEntity<Void> reschedule(@RequestBody RescheduleBody body) {
        var rescheduledAppointment = this.schedulingService.reschedule(body.id(), new DayHour(body.date(), body.time()));
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(rescheduledAppointment.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .build();
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable("id") Long id) {
        this.appointmentService.confirm(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable("id") Long id, @RequestBody Reason body) {
        this.appointmentService.reject(id, body.reason());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("id") Long id, @RequestBody Reason body) {
        this.appointmentService.cancel(id, body.reason());
        return ResponseEntity.noContent().build();
    }

}
