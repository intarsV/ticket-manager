package com.proofit.ticketmanager.domain;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class Ticket {

    @ApiModelProperty(value = "Passenger info")
    private final Passenger passenger;
    @ApiModelProperty(value = "Price for passenger", example = "12.1")
    private final BigDecimal passengerPrice;
    @ApiModelProperty(value = "Price for luggage", example = "3.63")
    private final BigDecimal luggagePrice;
    @ApiModelProperty(value = "Total of passenger price and luggage price", example = "15.73")
    private final BigDecimal total;

    public Ticket(Passenger passenger, BigDecimal passengerPrice, BigDecimal luggagePrice, BigDecimal total) {
        this.passenger = passenger;
        this.passengerPrice = passengerPrice;
        this.luggagePrice = luggagePrice;
        this.total = total;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public BigDecimal getPassengerPrice() {
        return passengerPrice;
    }

    public BigDecimal getLuggagePrice() {
        return luggagePrice;
    }

    public BigDecimal getTotal() {
        return total;
    }


}
