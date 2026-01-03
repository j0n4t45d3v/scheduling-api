package com.scheduling.api.controller;

import com.scheduling.api.domain.OfferedService;
import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.service.OfferedServiceService;
import com.scheduling.api.service.SchedulingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@RestController
@RequestMapping("/services")
public class OfferedServiceController {

    private final SchedulingService schedulingService;
    private final OfferedServiceService offeredServiceService;

    public OfferedServiceController(
            SchedulingService schedulingService,
            OfferedServiceService offeredServiceService
    ) {
        this.schedulingService = schedulingService;
        this.offeredServiceService = offeredServiceService;
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

    public record CreateServiceDTO(String name, String description, Set<WeekDays> workDays, Set<LocalTime> schedules) {
        public OfferedService toDomain() {
            var builder = OfferedService.builder()
                    .setName(name)
                    .setDescription(description);
            workDays.forEach(builder::addWorkDay);
            schedules.forEach(schedule -> builder.addSchedule(new Schedule(schedule)));
            return builder.build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateServiceDTO createService) {
        var offeredServiceCreated = this.offeredServiceService.create(createService.toDomain());
        var location = UriComponentsBuilder
                .fromPath("/service/{id}")
                .buildAndExpand(offeredServiceCreated.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .build();
    }
}
