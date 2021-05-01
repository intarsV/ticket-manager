package com.proofit.ticketmanager.service;

import com.proofit.ticketmanager.domain.*;
import com.proofit.ticketmanager.exception.TicketManagerException;
import com.proofit.ticketmanager.service.clients.BasePriceClient;
import com.proofit.ticketmanager.service.clients.TaxClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.proofit.ticketmanager.service.util.Constants.VARIABLES_ARE_NULL_CHECK_CLIENT_RESPONSES;

@Service
public class ServiceFacade {

    private static final double CHILD_DISCOUNT = 0.5;
    private static final double LUGGAGE_DISCOUNT = 0.3;

    private final BasePriceClient basePriceClient;
    private final TaxClient taxClient;

    @Autowired
    public ServiceFacade(BasePriceClient basePriceClient, TaxClient taxClient) {
        this.basePriceClient = basePriceClient;
        this.taxClient = taxClient;
    }

    public TicketResponse calculateOrderPrice(final TicketRequest ticketRequest) {
        final BigDecimal tax = taxClient.getTax();
        final BigDecimal basePrice = basePriceClient.getBasePrice(ticketRequest.getDestination());

        if (tax == null || basePrice == null) {
            throw new TicketManagerException(VARIABLES_ARE_NULL_CHECK_CLIENT_RESPONSES);
        }

        final List<Ticket> ticketList = createTicketsList(ticketRequest.getPassengerList(), tax, basePrice);
        return createResponse(ticketList);
    }

    private List<Ticket> createTicketsList(
            final List<Passenger> passengerList,
            final BigDecimal tax,
            final BigDecimal basePrice) {
        final List<Ticket> ticketList = new ArrayList<>();
        for (Passenger passenger : passengerList) {
            ticketList.add(createTicket(passenger, basePrice, tax));
        }
        return ticketList;
    }

    private Ticket createTicket(final Passenger passenger, final BigDecimal basePrice, final BigDecimal tax) {
        final BigDecimal passengerPrice = addTaxToPrice(calculatePassengerBasePrice(passenger, basePrice), tax);
        final BigDecimal luggagePrice = addTaxToPrice(calculateLuggagePrice(passenger, basePrice), tax);
        final BigDecimal total = passengerPrice.add(luggagePrice).setScale(2, RoundingMode.HALF_UP);
        return new Ticket(passenger, passengerPrice, luggagePrice, total);
    }

    private BigDecimal calculatePassengerBasePrice(final Passenger passenger, final BigDecimal basePrice) {
        switch (passenger.getPassengerType()) {
            case ADULT:
                return basePrice.setScale(2, RoundingMode.HALF_UP);
            case CHILD:
                return basePrice.subtract(basePrice.multiply(BigDecimal.valueOf(CHILD_DISCOUNT)))
                        .setScale(2, RoundingMode.HALF_UP);
            case PENSIONER:
            default:
                return BigDecimal.valueOf(0);
        }
    }

    private BigDecimal calculateLuggagePrice(final Passenger passenger, final BigDecimal basePrice) {
        return basePrice
                .multiply(BigDecimal.valueOf(LUGGAGE_DISCOUNT))
                .multiply(BigDecimal.valueOf(passenger.getLuggageUnits()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal addTaxToPrice(final BigDecimal value, final BigDecimal tax) {
        return value.add(value.multiply(tax));
    }

    private TicketResponse createResponse(final List<Ticket> ticketList) {
        final BigDecimal total = calculateTotalPrice(ticketList);
        return new TicketResponse(ticketList, total);
    }

    private BigDecimal calculateTotalPrice(final List<Ticket> list) {
        BigDecimal total = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        for (Ticket t : list) {
            total = total.add(t.getTotal());
        }
        return total;
    }
}
