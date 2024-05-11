package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.enums.DateRange;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsDetail {
    private DateRange range;
    private BigDecimal totalAmount;
    private List<StatisticsCategory> statisticsCategories;

}
