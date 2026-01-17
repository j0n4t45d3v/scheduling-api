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