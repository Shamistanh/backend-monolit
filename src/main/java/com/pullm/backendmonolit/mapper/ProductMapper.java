package com.pullm.backendmonolit.mapper;

import com.pullm.backendmonolit.models.response.PopularProductResponse;
import com.pullm.backendmonolit.models.response.PopularProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "icon", source = "productType.icon")
    PopularProductResponseDto mapToPopularProductResponseDto(PopularProductResponse popularProductResponse);

}
