package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.enums.DateRange;
import com.pullm.backendmonolit.models.response.StatisticsDetail;
import com.pullm.backendmonolit.services.StatisticsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/overview")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public StatisticsDetail getAllChartResponse(@RequestParam DateRange dateRange) {
        return statisticsService.getStatisticsDetail(dateRange);
    }

}
