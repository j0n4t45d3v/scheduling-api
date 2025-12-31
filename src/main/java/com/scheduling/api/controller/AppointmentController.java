package com.scheduling.api.controller;

import com.scheduling.api.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable("id") Long id) {
        this.appointmentService.confirm(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        this.appointmentService.reject(id, body.get("reject-reason"));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        this.appointmentService.cancel(id, body.get("cancel-reason"));
        return ResponseEntity.noContent().build();
    }

}
