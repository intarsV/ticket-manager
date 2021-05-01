package com.proofit.ticketmanager.domain;

import java.math.BigDecimal;

public class Ticket {

    private Passenger passenger;

    private BigDecimal passengerPrice;

    private BigDecimal luggagePrice;

    private BigDecimal total;

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public BigDecimal getPassengerPrice() {
        return passengerPrice;
    }

    public void setPassengerPrice(BigDecimal passengerPrice) {
        this.passengerPrice = passengerPrice;
    }

    public BigDecimal getLuggagePrice() {
        return luggagePrice;
    }

    public void setLuggagePrice(BigDecimal luggagePrice) {
        this.luggagePrice = luggagePrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
