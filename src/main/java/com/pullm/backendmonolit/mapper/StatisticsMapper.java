package com.pullm.backendmonolit.mapper;

import com.pullm.backendmonolit.models.response.StatisticsProductResponse;
import com.pullm.backendmonolit.models.response.StatisticsProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {

    @Mapping(target = "icon", source = "productType.icon")
    StatisticsProductResponseDto mapToStatisticsResponseDto(StatisticsProductResponse statisticsProductResponse);

}
