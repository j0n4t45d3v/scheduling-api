package com.scheduling.api.dto.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleBody(Long service, LocalDate date, LocalTime time){}
