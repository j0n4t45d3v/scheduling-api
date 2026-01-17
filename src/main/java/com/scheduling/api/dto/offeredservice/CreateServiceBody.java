package com.scheduling.api.dto.offeredservice;

import com.scheduling.api.domain.OfferedService;
import com.scheduling.api.domain.dvo.Schedule;
import com.scheduling.api.domain.enumerates.WeekDays;

import java.time.LocalTime;
import java.util.Set;

public record CreateServiceBody(String name, String description, Set<WeekDays> workDays, Set<LocalTime> schedules) {
    public OfferedService toDomain() {
        var builder = OfferedService.builder()
                .setName(name)
                .setDescription(description);
        workDays.forEach(builder::addWorkDay);
        schedules.forEach(schedule -> builder.addSchedule(new Schedule(schedule)));
        return builder.build();
    }
}
