package com.scheduling.api.controller;

import com.scheduling.api.domain.OfferedService;
import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.dto.offeredservice.CreateServiceBody;
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

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateServiceBody createService) {
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
