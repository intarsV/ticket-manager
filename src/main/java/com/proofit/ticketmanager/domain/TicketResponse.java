package com.proofit.ticketmanager.domain;

import java.math.BigDecimal;
import java.util.List;

public class TicketResponse {

    private List<Ticket> ticketList;

    private BigDecimal total;

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
