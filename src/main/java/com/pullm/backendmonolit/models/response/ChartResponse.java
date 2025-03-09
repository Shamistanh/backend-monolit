package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.entities.enums.ProductType;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ChartResponse {
    private ProductType type;
    private BigDecimal price;
    private BigDecimal percentage;

    public ChartResponse(ProductType type, BigDecimal price) {
        this.type = type;
        this.price = price;
    }

    public ChartResponse(ProductType type, BigDecimal price, BigDecimal percentage) {
        this.type = type;
        this.price = price;
        this.percentage = percentage;
    }
}
