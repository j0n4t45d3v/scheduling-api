package com.scheduling.api.domain.dvo;

import java.time.LocalTime;

public record Schedule(LocalTime value) {

    private boolean isGreatOrEqualsStartTime(LocalTime time) {
        return this.value.isAfter(time) || this.value.equals(time);
    }

    private boolean isLessOrEqualsEndTime(LocalTime time) {
        return this.value.isBefore(time) || this.value.equals(time);
    }
}
