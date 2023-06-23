package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.entities.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
public class ChartResponse {
    private ProductType type;
    private BigDecimal price;

    public ChartResponse(ProductType type, BigDecimal price) {
        this.type = type;
        this.price = price;
    }
}
