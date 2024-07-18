package com.pullm.backendmonolit.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pullm.backendmonolit.enums.FinancialType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialStatusRequest {

    @JsonProperty("financialType")
    private FinancialType financialType;
    @JsonProperty("amount")
    private BigDecimal amount;

}
