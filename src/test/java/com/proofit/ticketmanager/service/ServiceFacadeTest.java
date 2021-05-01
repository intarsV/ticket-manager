package com.proofit.ticketmanager.service;

import com.proofit.ticketmanager.domain.Passenger;
import com.proofit.ticketmanager.domain.PassengerType;
import com.proofit.ticketmanager.domain.TicketRequest;
import com.proofit.ticketmanager.domain.TicketResponse;
import com.proofit.ticketmanager.exception.TicketManagerException;
import com.proofit.ticketmanager.service.clients.BasePriceClient;
import com.proofit.ticketmanager.service.clients.TaxClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static com.proofit.ticketmanager.service.util.Constants.VARIABLES_ARE_NULL_CHECK_CLIENT_RESPONSES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceFacadeTest {

    @Mock
    private BasePriceClient basePriceClient;
    @Mock
    private TaxClient taxClient;
    @InjectMocks
    private ServiceFacade facade;

    @ParameterizedTest
    @CsvSource(value = {"ADULT:15.73", "CHILD:9.68", "PENSIONER:3.63"}, delimiter = ':')
    void shouldCorrectlyCalculateForAdult(String passengerType, String expectedResult) {
        TicketRequest request = createTicketRequest(passengerType);
        when(taxClient.getTax()).thenReturn(BigDecimal.valueOf(0.21));
        when(basePriceClient.getBasePrice(anyString())).thenReturn(BigDecimal.valueOf(10));
        TicketResponse r = facade.calculateOrderPrice(request);
        assertEquals(new BigDecimal(expectedResult), r.getTotal());
    }

    @Test
    void CalcVariablesAreNull_shouldThrowException() {
        TicketRequest request = createTicketRequest("ADULT");
        TicketManagerException thrown = assertThrows(
                TicketManagerException.class,
                () -> facade.calculateOrderPrice(request),
                "Expected to throw TicketManagerException, but it didn't"
        );
        assertTrue(thrown.getMessage().contains(VARIABLES_ARE_NULL_CHECK_CLIENT_RESPONSES));
    }

    private TicketRequest createTicketRequest(String passengerType) {
        TicketRequest request = new TicketRequest();
        request.setPassengerList(Collections.singletonList(createPassenger(passengerType)));
        request.setDestination("SOMEWHERE");
        return request;
    }

    private Passenger createPassenger(String passengerType) {
        Passenger passenger = new Passenger();
        passenger.setPassengerType(PassengerType.valueOf(passengerType));
        passenger.setLuggageUnits(1);
        return passenger;
    }
}