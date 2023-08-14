package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.entities.enums.ProductSubType;
import com.pullm.backendmonolit.entities.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal quantity;
}
