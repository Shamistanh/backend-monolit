package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.enums.DateRange;
import com.pullm.backendmonolit.models.response.StatisticsDetail;

public interface StatisticsService {

    StatisticsDetail getStatisticsDetail(DateRange dateRange, ProductType productType);

}
