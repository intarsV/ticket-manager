package com.proofit.ticketmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proofit.ticketmanager.domain.TicketRequest;
import com.proofit.ticketmanager.domain.TicketResponse;
import com.proofit.ticketmanager.exception.TicketManagerException;
import com.proofit.ticketmanager.service.ServiceFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.proofit.ticketmanager.service.util.Constants.VARIABLES_ARE_NULL_CHECK_CLIENT_RESPONSES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TicketController.class)
class TicketControllerTest {

    private static final String URL_TEMPLATE = "/api/v1/ticket-manager";
    private final String errorResponseMessage = "{\"message\":\"%s\"}";
    private final String request = "{\"passengerList\":[{\"passengerType\": \"%s\",\"luggageUnits\":%s}],\"destination\":\"%s\"}";

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    ServiceFacade facade;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnResponse() throws Exception {
        String requestBody = String.format(request, "ADULT", 1, "Liepaja");
        when(facade.calculateOrderPrice(any(TicketRequest.class))).thenReturn(new TicketResponse());
        mockMvc.perform(post(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string((mapper.writeValueAsString(new TicketResponse()))));
    }

    @Test
    void shouldReturnResponse_ParsingError_WrongType() throws Exception {
        String requestBody = String.format(request, "UNKNOWN_TYPE", 1, "Liepaja");
        when(facade.calculateOrderPrice(any(TicketRequest.class))).thenReturn(new TicketResponse());
        mockMvc.perform(post(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format(errorResponseMessage, "JSON parsing failed")));
    }

    @Test
    void shouldReturnResponse_ParsingError_EmptyValue() throws Exception {
        String requestBody = String.format(request, "", 1, "Liepaja");
        when(facade.calculateOrderPrice(any(TicketRequest.class))).thenReturn(new TicketResponse());
        mockMvc.perform(post(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format(errorResponseMessage, "JSON parsing failed")));
    }

    @Test
    void shouldReturnResponse_ValidationError_NoDestination() throws Exception {
        String requestA = "{\"passengerList\":[{\"passengerType\": \"%s\",\"luggageUnits\":%s}]}";
        String requestBody = String.format(requestA, "ADULT", 1);
        when(facade.calculateOrderPrice(any(TicketRequest.class))).thenReturn(new TicketResponse());
        mockMvc.perform(post(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format(errorResponseMessage, "destination field validation failed")));
    }

    @Test
    void shouldReturnResponse_ValidationError_EmptyDestination() throws Exception {
        String requestBody = String.format(request, "ADULT", 1, "");
        when(facade.calculateOrderPrice(any(TicketRequest.class))).thenReturn(new TicketResponse());
        mockMvc.perform(post(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format(errorResponseMessage, "destination field validation failed")));
    }

    @Test
    void shouldReturnResponse_ValidationError_NegativeLuggageValue() throws Exception {
        String requestBody = String.format(request, "ADULT", -2, "Tallin");
        when(facade.calculateOrderPrice(any(TicketRequest.class))).thenReturn(new TicketResponse());
        mockMvc.perform(post(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format(errorResponseMessage, "passengerList[0].luggageUnits field validation failed")));
    }

    @Test
    void shouldReturnResponse_ClientServiceError() throws Exception {
        String requestBody = String.format(request, "ADULT", 1, "Liepaja");
        when(facade.calculateOrderPrice(any(TicketRequest.class)))
                .thenThrow(new TicketManagerException(VARIABLES_ARE_NULL_CHECK_CLIENT_RESPONSES));
        mockMvc.perform(post(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(String.format(errorResponseMessage, VARIABLES_ARE_NULL_CHECK_CLIENT_RESPONSES)));
    }
}