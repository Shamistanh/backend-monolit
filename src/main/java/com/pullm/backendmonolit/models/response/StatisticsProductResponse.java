package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.entities.enums.ProductSubType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsProductResponse {

    private LocalDateTime date;
    private String name;
    private BigDecimal price;
    private int count;
    private ProductSubType productSubType;

}
