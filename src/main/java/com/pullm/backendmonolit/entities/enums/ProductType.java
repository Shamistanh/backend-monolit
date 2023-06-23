package com.pullm.backendmonolit.entities.enums;

import lombok.Getter;

@Getter
public enum ProductType {
    FOOD_AND_BEVERAGES("Food and beverages"),
    SHOPPING("Shopping"),
    LIFE_AND_ENTERTAINMENT("Life and entertainment"),
    HOUSING_AND_VEHICLE("Housing and vehicle"),
    TRANSPORT("Transport"),
    OTHERS("Others");

    private final String value;

    ProductType(String value) {
        this.value = value;
    }

}
