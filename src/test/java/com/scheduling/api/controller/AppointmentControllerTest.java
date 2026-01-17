package com.scheduling.api.controller;

import com.scheduling.api.domain.Appointment;
import com.scheduling.api.domain.dvo.DayHour;
import com.scheduling.api.domain.exceptions.DomainException;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.service.AppointmentService;
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

import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;
    @MockitoBean
    private SchedulingService schedulingService;

    @Test
    @DisplayName("should return 201 when service scheduling is done successfully")
    void shouldReturn201WhenServiceSchedulingIsDoneSuccessfully() throws Exception {
        Appointment appointmentMock = mock(Appointment.class);
        when(appointmentMock.getId())
                .thenReturn(1L);
        when(this.schedulingService.schedule(anyLong(), any(DayHour.class)))
                .thenReturn(appointmentMock);
        this.mockMvc
                .perform(post("/appointments")
                        .contentType("application/json")
                        .content("{\"service\": 1,\"date\":\"1999-12-12\", \"time\":\"11:00\"}"))
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
                .perform(post("/appointments")
                        .contentType("application/json")
                        .content("{\"service\": 1,\"date\":\"1999-12-12\", \"time\":\"11:00\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when service scheduling violate domain rules")
    void shouldReturn400WhenServiceSchedulingViolateDomainRules() throws Exception {
        doThrow(DomainException.class)
                .when(this.schedulingService)
                .schedule(anyLong(), any(DayHour.class));

        this.mockMvc
                .perform(post("/appointments")
                        .contentType("application/json")
                        .content("{\"service\": 1,\"date\":\"1999-12-12\", \"time\":\"11:00\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 201 when rescheduling is done successfully")
    void shouldReturn201WhenReschedulingIsDoneSuccessfully() throws Exception {
        Appointment appointmentMock = mock(Appointment.class);
        when(appointmentMock.getId())
                .thenReturn(1L);
        when(this.schedulingService.reschedule(anyLong(), any(DayHour.class)))
                .thenReturn(appointmentMock);
        this.mockMvc
                .perform(post("/appointments/reschedule")
                        .contentType("application/json")
                        .content("{\"id\": 1,\"date\":\"1999-12-12\", \"time\":\"11:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));

        LocalDate day = LocalDate.of(1999, 12, 12);
        LocalTime hour = LocalTime.of(11, 0);
        DayHour appointment = new DayHour(day, hour);
        verify(this.schedulingService).reschedule(1L, appointment);
    }

    @Test
    @DisplayName("should return 404 when not found appointment in reschedule")
    void shouldReturn404WhenNotFoundAppointmentInReschedule() throws Exception {
        doThrow(new NotFoundRecordException("fail"))
                .when(this.schedulingService)
                .reschedule(anyLong(), any(DayHour.class));

        this.mockMvc
                .perform(post("/appointments/reschedule")
                        .contentType("application/json")
                        .content("{\"id\": 1,\"date\":\"1999-12-12\", \"time\":\"11:00\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when rescheduling violate domain rules")
    void shouldReturn400WhenReschedulingViolateDomainRules() throws Exception {
        doThrow(DomainException.class)
                .when(this.schedulingService)
                .reschedule(anyLong(), any(DayHour.class));

        this.mockMvc
                .perform(post("/appointments/reschedule")
                        .contentType("application/json")
                        .content("{\"id\": 1,\"date\":\"1999-12-12\", \"time\":\"11:00\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("should return 204 when confirm valid appointment")
    void shouldReturn204WhenConfirmValidAppointment() throws Exception {
        this.mockMvc.perform(put("/appointments/{id}/confirm", 1))
                .andExpect(status().isNoContent());

        verify(this.appointmentService).confirm(1L);
    }

    @Test
    @DisplayName("should return 404 when not found confirm appointment provided")
    void shouldReturn404WhenNotFoundConfirmAppointmentProvided() throws Exception {
        doThrow(new NotFoundRecordException("fail"))
                .when(this.appointmentService)
                .confirm(anyLong());

        this.mockMvc.perform(put("/appointments/{id}/confirm", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when confirm appointment violate domain rules")
    void shouldReturn400WhenConfirmAppointmentViolateDomainRules() throws Exception {
        doThrow(DomainException.class)
                .when(this.appointmentService)
                .confirm(anyLong());

        this.mockMvc.perform(put("/appointments/{id}/confirm", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 204 when reject valid appointment")
    void shouldReturn204WhenRejectValidAppointment() throws Exception {
        this.mockMvc.perform(
                put("/appointments/{id}/reject", 1)
                        .contentType("application/json")
                        .content("{\"reason\": \"test reason message\"}")
        ).andExpect(status().isNoContent());

        verify(this.appointmentService).reject(1L, "test reason message");
    }

    @Test
    @DisplayName("should return 404 when not found reject appointment provided")
    void shouldReturn404WhenNotFoundRejectAppointmentProvided() throws Exception {
        doThrow(new NotFoundRecordException("fail"))
                .when(this.appointmentService)
                .reject(anyLong(), anyString());

        this.mockMvc.perform(
                put("/appointments/{id}/reject", 1)
                        .contentType("application/json")
                        .content("{\"reason\": \"test reason message\"}")
        ).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when reject appointment violate domain rules")
    void shouldReturn400WhenRejectAppointmentViolateDomainRules() throws Exception {
        doThrow(DomainException.class)
                .when(this.appointmentService)
                .reject(anyLong(), anyString());

        this.mockMvc.perform(put("/appointments/{id}/reject", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 204 when cancel a valid appointment")
    void shouldReturn204WhenCancelAValidAppointment() throws Exception {
        this.mockMvc.perform(
                put("/appointments/{id}/cancel", 1)
                        .contentType("application/json")
                        .content("{\"reason\": \"test reason message\"}")
        ).andExpect(status().isNoContent());

        verify(this.appointmentService).cancel(1L, "test reason message");
    }

    @Test
    @DisplayName("should return 404 when not found cancel appointment provided")
    void shouldReturn404WhenNotFoundCancelAppointmentProvided() throws Exception {
        doThrow(new NotFoundRecordException("fail"))
                .when(this.appointmentService)
                .cancel(anyLong(), anyString());

        this.mockMvc.perform(
                put("/appointments/{id}/cancel", 1)
                        .contentType("application/json")
                        .content("{\"reason\": \"test reason message\"}")
        ).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when cancel appointment violate domain rules")
    void shouldReturn400WhenCancelAppointmentViolateDomainRules() throws Exception {
        doThrow(DomainException.class)
                .when(this.appointmentService)
                .cancel(anyLong(), anyString());

        this.mockMvc.perform(put("/appointments/{id}/cancel", 1))
                .andExpect(status().isBadRequest());
    }

}