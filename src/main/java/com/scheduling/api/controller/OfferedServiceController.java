package com.scheduling.api.controller;

import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.service.SchedulingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/services")
public class OfferedServiceController {

    private final SchedulingService schedulingService;

    public OfferedServiceController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    public record ScheduleServiceDTO(LocalDate day, LocalTime hour) {
        public DayHour toDayHour() {
            return new DayHour(day, hour);
        }
    }

    @PostMapping("/{id}/appointments")
    public ResponseEntity<Void> schedule(@PathVariable("id") Long id, @RequestBody ScheduleServiceDTO scheduleServiceDTO) {
        var appointment = this.schedulingService.schedule(id, scheduleServiceDTO.toDayHour());
        var location = UriComponentsBuilder
                .fromPath("/appointments/{id}")
                .buildAndExpand(appointment.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .build();
    }
}
