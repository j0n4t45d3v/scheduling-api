package com.scheduling.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduling.api.domain.Appointment;
import com.scheduling.api.domain.OfferedService;
import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.enumerates.WeekDays;
import com.scheduling.api.domain.exceptions.DomainException;
import com.scheduling.api.infra.errors.bussines.ConflictRecordException;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.service.OfferedServiceService;
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
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfferedServiceController.class)
class OfferedServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SchedulingService schedulingService;

    @MockitoBean
    private OfferedServiceService offeredServiceService;

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

    @Test
    @DisplayName("should return 201 when service created it is valid")
    void shouldReturn201WhenServiceCreatedItIsValid() throws Exception {
        var bodyRequest = new OfferedServiceController.CreateServiceDTO(
                "name",
                "description",
                Set.of(WeekDays.SUNDAY),
                Set.of(LocalTime.now())
        );

        when(this.offeredServiceService.create(any(OfferedService.class)))
                .thenReturn(mock(OfferedService.class));
        this.mockMvc
                .perform(post("/services")
                        .contentType("application/json")
                        .content(this.objectMapper.writeValueAsString(bodyRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));
        verify(this.offeredServiceService, times(1)).create(any(OfferedService.class));
    }

    @Test
    @DisplayName("should return 409 when service name is already used")
    void shouldReturn409WhenServiceNameIsAlreadyUsed() throws Exception {
        var bodyRequest = new OfferedServiceController.CreateServiceDTO(
                "name",
                "description",
                Set.of(WeekDays.SUNDAY),
                Set.of(LocalTime.now())
        );

        doThrow(new ConflictRecordException("error")).when(this.offeredServiceService).create(any(OfferedService.class));
        this.mockMvc
                .perform(post("/services")
                        .contentType("application/json")
                        .content(this.objectMapper.writeValueAsString(bodyRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("should return 400 when service provided not meet domain rules")
    void shouldReturn400WhenServiceProvidedNotMeetDomainRules() throws Exception{
        var bodyRequest = new OfferedServiceController.CreateServiceDTO(
                "name",
                "description",
                Set.of(),
                Set.of(LocalTime.now())
        );
        doThrow(DomainException.class).when(this.offeredServiceService).create(any(OfferedService.class));

        this.mockMvc
                .perform(post("/services")
                        .contentType("application/json")
                        .content(this.objectMapper.writeValueAsString(bodyRequest)))
                .andExpect(status().isBadRequest());
    }


}