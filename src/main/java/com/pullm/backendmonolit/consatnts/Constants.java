package com.pullm.backendmonolit.consatnts;

import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.entities.enums.ProductSubType;
import com.pullm.backendmonolit.exception.ResourceNotFoundException;
import lombok.Getter;

import java.util.List;
import java.util.Map;

import static com.pullm.backendmonolit.entities.enums.ProductType.*;
import static com.pullm.backendmonolit.entities.enums.ProductSubType.*;

@Getter
public class Constants {

    private static final Map<ProductType, List<ProductSubType>> TYPES_TO_SUB_TYPES = Map.of(
            FOOD_AND_BEVERAGES,      List.of(GROCERIES, RESTAURANT, BAR),
            SHOPPING,                List.of(CLOTHES, ACCESSORIES, BEAUTY, HOME, PETS, ELECTRONICS, GIFT),
            LIFE_AND_ENTERTAINMENT,  List.of(HEALTH_CARE, FITNESS, EDUCATION, HOBBIES, LOTTERY, HOTEL),
            HOUSING_AND_VEHICLE,     List.of(RENT, UTILITY, REPAIR, FUEL, PARKING),
            TRANSPORT,               List.of(PUBLIC_TRANSPORT, TAXI, FLIGHTS),
            OTHERS,                  List.of(TAXES, INSURANCE, PENALTY, CHARITY, INVESTMENT, BANKING)
    );


    public static ProductType findProductTypeBySubType(ProductSubType subType) {
        return TYPES_TO_SUB_TYPES.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(subType))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(( ) -> new ResourceNotFoundException("Product Type not found by subType: " + subType));
    }


}
