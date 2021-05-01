package com.proofit.ticketmanager.controller;

import com.proofit.ticketmanager.domain.TicketRequest;
import com.proofit.ticketmanager.domain.TicketResponse;
import com.proofit.ticketmanager.service.ServiceFacade;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/ticket-manager")
@Validated
public class TicketController {

    private final ServiceFacade facade;

    @Autowired
    public TicketController(ServiceFacade facade) {
        this.facade = facade;
    }

    @ApiOperation(value = "calculate ticket price", nickname = "calculatePrice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = TicketResponse.class)
    })
    @PostMapping
    @ResponseBody
    public TicketResponse calculateOrderPrice(@Valid @RequestBody final TicketRequest ticketRequest) {
        return facade.calculateOrderPrice(ticketRequest);
    }
}
