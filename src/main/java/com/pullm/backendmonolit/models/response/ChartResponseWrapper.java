package com.pullm.backendmonolit.models.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartResponseWrapper {

    List<ChartResponse> allChartResponse;
    private BigDecimal totalExpense;

}
