package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.entities.enums.ProductSubType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsProductResponseDto {

    private LocalDateTime date;
    private String name;
    private BigDecimal price;
    private BigDecimal weight;
    private Integer count;
    private String icon;
    @Enumerated(EnumType.STRING)
    private ProductSubType productSubType;

    public StatisticsProductResponseDto(LocalDateTime date, String name, BigDecimal price, BigDecimal weight,
                                        Integer count, ProductSubType productSubType) {
        this.date = date;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.count = count;
        this.productSubType = productSubType;
    }

}
