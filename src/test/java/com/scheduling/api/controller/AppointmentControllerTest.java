package com.scheduling.api.controller;

import com.scheduling.api.domain.exceptions.DomainException;
import com.scheduling.api.infra.errors.bussines.NotFoundRecordException;
import com.scheduling.api.service.AppointmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;


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
                        .content("{\"reject-reason\": \"test reason message\"}")
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
                        .content("{\"reject-reason\": \"test reason message\"}")
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

}