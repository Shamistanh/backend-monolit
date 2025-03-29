package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.entities.enums.ProductSubType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsProductResponse {

    private LocalDateTime date;
    private String name;
    private BigDecimal price;
    private BigDecimal weight;
    private Integer count;
    private ProductSubType productSubType;

}
