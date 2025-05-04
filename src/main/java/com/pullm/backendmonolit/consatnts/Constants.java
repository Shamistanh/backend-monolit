package com.pullm.backendmonolit.consatnts;

import static com.pullm.backendmonolit.entities.enums.ProductSubType.ACCESSORIES;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.BANKING;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.BAR;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.BEAUTY;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.CHARITY;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.CLOTHES;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.EDUCATION;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.ELECTRONICS;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.FITNESS;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.FLIGHTS;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.FUEL;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.GIFT;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.GROCERIES;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.HEALTH_CARE;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.HOBBIES;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.HOME;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.HOTEL;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.INSURANCE;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.INVESTMENT;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.LOTTERY;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.OTHER;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.PARKING;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.PENALTY;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.PETS;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.PUBLIC_TRANSPORT;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.RENT;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.REPAIR;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.RESTAURANT;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.TAXES;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.TAXI;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.UTILITY;
import static com.pullm.backendmonolit.entities.enums.ProductType.FOOD_AND_BEVERAGES;
import static com.pullm.backendmonolit.entities.enums.ProductType.HOUSING_AND_VEHICLE;
import static com.pullm.backendmonolit.entities.enums.ProductType.LIFE_AND_ENTERTAINMENT;
import static com.pullm.backendmonolit.entities.enums.ProductType.OTHERS;
import static com.pullm.backendmonolit.entities.enums.ProductType.SHOPPING;
import static com.pullm.backendmonolit.entities.enums.ProductType.TRANSPORT;

import com.pullm.backendmonolit.entities.enums.ProductSubType;
import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.exception.NotFoundException;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class Constants {

    private static final Map<ProductType, List<ProductSubType>> TYPES_TO_SUB_TYPES = Map.of(
            FOOD_AND_BEVERAGES, List.of(GROCERIES, RESTAURANT, BAR),
            SHOPPING, List.of(CLOTHES, ACCESSORIES, BEAUTY, HOME, PETS, ELECTRONICS, GIFT),
            LIFE_AND_ENTERTAINMENT, List.of(HEALTH_CARE, FITNESS, EDUCATION, HOBBIES, LOTTERY, HOTEL),
            HOUSING_AND_VEHICLE, List.of(RENT, UTILITY, REPAIR, FUEL, PARKING),
            TRANSPORT, List.of(PUBLIC_TRANSPORT, TAXI, FLIGHTS),
            OTHERS, List.of(TAXES, INSURANCE, PENALTY, CHARITY, INVESTMENT, BANKING, OTHER)
    );

    public static List<ProductSubType> findProductTypeByType(ProductType productType) {
        return TYPES_TO_SUB_TYPES.get(productType);
    }

    public static ProductType findProductTypeBySubType(ProductSubType subType) {
        return TYPES_TO_SUB_TYPES.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(subType))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundException("Product Type not found by subType: " + subType));
    }


}
