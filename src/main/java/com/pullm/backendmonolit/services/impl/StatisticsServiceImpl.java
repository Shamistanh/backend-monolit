package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.enums.DateRange;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.response.ChartSingleResponse;
import com.pullm.backendmonolit.models.response.StatisticsDetail;
import com.pullm.backendmonolit.repository.ProductRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.StatisticsService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    public StatisticsDetail getStatisticsDetail(DateRange dateRange, ProductType productType) {
        StatisticsDetail statisticsDetail = null;
        switch (dateRange) {
            case DAILY -> statisticsDetail = getDailyData(productType);
            case MONTHLY -> statisticsDetail = getMonthlyData(productType);
            case YEARLY -> statisticsDetail = getYearlyData(productType);
        }
        return statisticsDetail;
    }

    private StatisticsDetail getYearlyData(ProductType productType) {
        List<ChartSingleResponse> statisticsDetailsByYear =
                productRepository.getStatisticsDetailsByYear(productType, getUser().getId());

        return getStatisticsResponse(statisticsDetailsByYear, DateRange.YEARLY);
    }

    private StatisticsDetail getMonthlyData(ProductType productType) {
        List<ChartSingleResponse> statisticsDetailsByMonth =
                productRepository.getStatisticsDetailsByMonth(productType, getUser().getId());

        return getStatisticsResponse(statisticsDetailsByMonth, DateRange.MONTHLY);

    }

    private StatisticsDetail getDailyData(ProductType productType) {
        List<ChartSingleResponse> statisticsDetailsByWeek =
                productRepository.getStatisticsDetailsByDay(productType, getUser().getId());

        return getStatisticsResponse(statisticsDetailsByWeek, DateRange.DAILY);
    }

    private StatisticsDetail getStatisticsResponse(List<ChartSingleResponse> chartValues, DateRange dateRange) {
        return StatisticsDetail.builder()
                .chartDetails(chartValues)
                .range(dateRange)
                .totalPrice(chartValues.stream()
                        .map(ChartSingleResponse::getAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }


    private User getUser() {
        var email = extractEmail();
        var user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email not found"));

        log.info("getUser(): user-id: " + user.getId());

        return user;
    }

    private String extractEmail() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

}
