package com.proofit.ticketmanager.domain;

import java.math.BigDecimal;

public class TaxClientResponse {

    private BigDecimal tax;

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}
