package com.proofit.ticketmanager.domain;

import java.math.BigDecimal;

public class BasePriceClientResponse {

    private BigDecimal basePrice;

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
}
