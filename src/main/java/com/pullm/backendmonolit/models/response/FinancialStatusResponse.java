package com.pullm.backendmonolit.models.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialStatusResponse {

    private BigDecimal balance;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;
    private String currency;
    private Long userId;
}
