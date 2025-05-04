package com.pullm.backendmonolit.models.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyResponse {
    private String flag;
    private String currencyCode;
    private String symbol;
    private Double rate;
}
