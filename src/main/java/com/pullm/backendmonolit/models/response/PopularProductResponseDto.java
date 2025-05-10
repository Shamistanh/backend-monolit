package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.entities.enums.ProductType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PopularProductResponseDto {

    private String name;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    private String icon;
    private Long count;

}
