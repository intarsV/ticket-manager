package com.proofit.ticketmanager.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Passenger {

    @NotNull
    @ApiModelProperty(value = "Passenger type", allowableValues = "ADULT, CHILD, PENSIONER")
    private final PassengerType passengerType;

    @Min(value = 0, message = "Minimal luggage units should be zero!")
    @ApiModelProperty(value = "Luggage units for passenger", example = "1")
    private final int luggageUnits;

    public Passenger(@NotNull PassengerType passengerType,
                     @Min(value = 0, message = "Minimal luggage units should be zero!") int luggageUnits) {
        this.passengerType = passengerType;
        this.luggageUnits = luggageUnits;
    }

    public PassengerType getPassengerType() {
        return passengerType;
    }

    public int getLuggageUnits() {
        return luggageUnits;
    }

}
