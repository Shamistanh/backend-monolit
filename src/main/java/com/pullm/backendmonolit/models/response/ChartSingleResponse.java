package com.pullm.backendmonolit.models.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ChartSingleResponse {

    private String order;
    private BigDecimal amount;

    public ChartSingleResponse(String order, BigDecimal amount) {
        this.order = order;
        this.amount = amount;
    }

}
