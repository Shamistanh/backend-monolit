package com.pullm.backendmonolit.entities.enums;

import lombok.Getter;

@Getter
public enum ProductType {
    FOOD_AND_BEVERAGES("Food and beverages", "#5EFF33"),
    SHOPPING("Shopping", "#FF33FF"),
    LIFE_AND_ENTERTAINMENT("Life and entertainment", "#33B5FF"),
    HOUSING_AND_VEHICLE("Housing and vehicle", "#FF3333"),
    TRANSPORT("Transport", "#FF3333"),
    OTHERS("Others", "#8C8B88");

    private final String value;

    private final String color;

    ProductType(String value, String color) {
        this.value = value;
        this.color = color;
    }

}
