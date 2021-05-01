package com.proofit.ticketmanager.domain;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

public class TicketResponse {
    @ApiModelProperty(value = "List of tickets")
    private final List<Ticket> ticketList;
    @ApiModelProperty(value = "Total of all tickets", example = "15.73")
    private final BigDecimal total;

    public TicketResponse(List<Ticket> ticketList, BigDecimal total) {
        this.ticketList = ticketList;
        this.total = total;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public BigDecimal getTotal() {
        return total;
    }

}
