package com.pullm.backendmonolit.models.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialStatusOverallResponse {

    private BigDecimal balance;
    private BigDecimal income;
    private BigDecimal expense;
    private String currency;
    private Long userId;

}
