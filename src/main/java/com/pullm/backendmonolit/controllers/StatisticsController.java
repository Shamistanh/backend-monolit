package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.entities.enums.ProductSubType;
import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.enums.DateRange;
import com.pullm.backendmonolit.models.response.ResponseDTO;
import com.pullm.backendmonolit.models.response.StatisticsDetail;
import com.pullm.backendmonolit.models.response.StatisticsProductResponse;
import com.pullm.backendmonolit.services.StatisticsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/overview")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<StatisticsDetail> getAllChartResponse(@RequestParam DateRange dateRange,
                                                             @RequestParam(required = false) LocalDateTime startDate,
                                                             @RequestParam(required = false) LocalDateTime endDate) {
        return ResponseDTO.<StatisticsDetail>builder().data(statisticsService.getStatisticsDetail(dateRange,
                startDate,endDate)).build();
    }

    @GetMapping("/products")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<List<StatisticsProductResponse>> getProducts(@RequestParam(required = false) ProductSubType productSubType, @RequestParam
    ProductType productType) {
        return ResponseDTO.<List<StatisticsProductResponse>>builder().data(statisticsService.getProductDetails(productSubType,
                productType)).build();
    }

}
