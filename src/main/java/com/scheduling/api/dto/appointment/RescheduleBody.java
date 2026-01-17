package com.scheduling.api.dto.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleBody(Long id, LocalDate date, LocalTime time){}
