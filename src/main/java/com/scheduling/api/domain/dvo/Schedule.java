package com.scheduling.api.domain.dvo;

import java.time.LocalTime;

public record Schedule(LocalTime value) {

    public boolean isEquals(LocalTime time) {
        return this.value.equals(time);
    }

    private boolean isGreatOrEqualsStartTime(LocalTime time) {
        return this.value.isAfter(time) || this.value.equals(time);
    }

    private boolean isLessOrEqualsEndTime(LocalTime time) {
        return this.value.isBefore(time) || this.value.equals(time);
    }
}
