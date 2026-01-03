package com.scheduling.api.controller;

import com.scheduling.api.domain.Appointment;
import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.exceptions.DomainException;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.service.SchedulingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfferedServiceController.class)
class OfferedServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SchedulingService schedulingService;

    @Test
    @DisplayName("should return 204 when service scheduling is done successfully")
    void shouldReturn204WhenServiceSchedulingIsDoneSuccessfully() throws Exception {
        Appointment appointmentMock = mock(Appointment.class);
        when(appointmentMock.getId())
                .thenReturn(1L);
        when(this.schedulingService.schedule(anyLong(), any(DayHour.class)))
                .thenReturn(appointmentMock);
        this.mockMvc
                .perform(post("/services/{id}/appointments", 1)
                        .contentType("application/json")
                        .content("{\"day\":\"1999-12-12\", \"hour\":\"11:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));

        LocalDate day = LocalDate.of(1999, 12, 12);
        LocalTime hour = LocalTime.of(11, 0);
        DayHour appointment = new DayHour(day, hour);
        verify(this.schedulingService).schedule(1L, appointment);
    }

    @Test
    @DisplayName("should return 404 when not found service of the scheduling")
    void shouldReturn404WhenNotFoundServiceOfTheScheduling() throws Exception {
        doThrow(new NotFoundRecordException("fail"))
                .when(this.schedulingService)
                .schedule(anyLong(), any(DayHour.class));

        this.mockMvc
                .perform(post("/services/{id}/appointments", 1)
                        .contentType("application/json")
                        .content("{\"day\":\"1999-12-12\", \"hour\":\"11:00\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when service scheduling violate domain rules")
    void shouldReturn400WhenServiceSchedulingViolateDomainRules() throws Exception {
        doThrow(DomainException.class)
                .when(this.schedulingService)
                .schedule(anyLong(), any(DayHour.class));

        this.mockMvc
                .perform(post("/services/{id}/appointments", 1)
                        .contentType("application/json")
                        .content("{\"day\":\"1999-12-12\", \"hour\":\"11:00\"}"))
                .andExpect(status().isBadRequest());
    }

}