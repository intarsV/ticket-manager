package com.proofit.ticketmanager.exception;

public class TicketManagerException extends RuntimeException {

    public TicketManagerException(String message, Exception e) {
        super(message, e);
    }

    public TicketManagerException(String message) {
        super(message);
    }
}
