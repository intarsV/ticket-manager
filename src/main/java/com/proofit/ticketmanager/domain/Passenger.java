package com.proofit.ticketmanager.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Passenger {

    @NotNull
    private PassengerType passengerType;

    @Min(value = 0, message = "Minimal luggage units should be zero!")
    private int luggageUnits;

    public PassengerType getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(PassengerType passengerType) {
        this.passengerType = passengerType;
    }

    public int getLuggageUnits() {
        return luggageUnits;
    }

    public void setLuggageUnits(int luggageUnits) {
        this.luggageUnits = luggageUnits;
    }
}
