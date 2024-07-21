package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.entities.enums.ProductSubType;
import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.enums.DateRange;
import com.pullm.backendmonolit.models.response.StatisticsDetail;
import com.pullm.backendmonolit.models.response.StatisticsProductResponse;
import java.util.List;

public interface StatisticsService {

    StatisticsDetail getStatisticsDetail(DateRange dateRange);

    List<StatisticsProductResponse> getProductDetails(ProductSubType productSubType, ProductType productType);

}
