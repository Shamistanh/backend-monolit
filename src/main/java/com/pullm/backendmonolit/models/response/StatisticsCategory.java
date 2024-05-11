package com.pullm.backendmonolit.models.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@Setter
public class StatisticsCategory {
    private String productType;
    private BigDecimal productBasedTotalPrice;
    private BigDecimal percentage;
    private List<ChartSingleResponse> chartDetails;

}
