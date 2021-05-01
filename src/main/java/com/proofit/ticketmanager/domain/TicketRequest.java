package com.proofit.ticketmanager.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class TicketRequest {

    @Valid
    @NotEmpty
    @ApiModelProperty(value = "List of passenger")
    private final List<Passenger> passengerList;

    @NotBlank
    @ApiModelProperty(value = "Route destination", example = "Munamegis")
    private final String destination;

    public TicketRequest(@Valid @NotEmpty List<Passenger> passengerList, @NotBlank String destination) {
        this.passengerList = passengerList;
        this.destination = destination;
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public String getDestination() {
        return destination;
    }

}
