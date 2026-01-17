package com.scheduling.api.controller;

import com.scheduling.api.dto.offeredservice.CreateServiceBody;
import com.scheduling.api.service.OfferedServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/services")
public class OfferedServiceController {

    private final OfferedServiceService offeredServiceService;

    public OfferedServiceController(
            OfferedServiceService offeredServiceService
    ) {
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
