package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.entities.enums.ProductSubType;
import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.enums.DateRange;
import com.pullm.backendmonolit.models.response.StatisticsDetail;
import com.pullm.backendmonolit.models.response.StatisticsProductResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

public interface StatisticsService {

    StatisticsDetail getStatisticsDetail(DateRange dateRange, LocalDateTime startDate, LocalDateTime endDate);

    List<StatisticsProductResponse> getProductDetails(ProductSubType productSubType, ProductType productType);

}
