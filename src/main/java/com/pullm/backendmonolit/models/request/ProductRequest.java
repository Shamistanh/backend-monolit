package com.pullm.backendmonolit.models.request;

import com.pullm.backendmonolit.entities.enums.ProductSubType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private Integer count;
    private BigDecimal weight;
    private ProductSubType productSubType;
}
