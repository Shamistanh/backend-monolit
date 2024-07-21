package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.entities.enums.ProductSubType;
import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.enums.DateRange;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.response.ChartSingleResponse;
import com.pullm.backendmonolit.models.response.StatisticsCategory;
import com.pullm.backendmonolit.models.response.StatisticsDetail;
import com.pullm.backendmonolit.models.response.StatisticsProductResponse;
import com.pullm.backendmonolit.repository.ProductRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.StatisticsService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
    public StatisticsDetail getStatisticsDetail(DateRange dateRange, LocalDateTime startDate, LocalDateTime endDate) {
        StatisticsDetail statisticsDetail = null;
        switch (dateRange) {
            case DAILY -> statisticsDetail = getDailyData(startDate,endDate);
            case MONTHLY -> statisticsDetail = getMonthlyData(startDate,endDate);
            case YEARLY -> statisticsDetail = getYearlyData(startDate,endDate);
        }
        return statisticsDetail;
    }

    @Override
    public List<StatisticsProductResponse> getProductDetails(ProductSubType productSubType, ProductType productType) {
        return productRepository.findAllByProductTypeAndSubtype(productSubType,productType, getUser());
    }

    private StatisticsDetail getYearlyData(LocalDateTime startDate, LocalDateTime endDate) {
        StatisticsDetail statisticsDetail = StatisticsDetail.builder()
                .statisticsCategories(
                        productRepository.getAllProductTypes(getUser().getId(), startDate, endDate).stream().map(productType -> {
                            List<ChartSingleResponse> statisticsDetailsByMonth =
                                    productRepository.getStatisticsDetailsByYear(ProductType.valueOf(productType),
                                            getUser().getId());
                            return getStatisticsCategoryResponse(statisticsDetailsByMonth, productType);
                        }).collect(Collectors.toList()))
                .range(DateRange.YEARLY)
                .build();

        statisticsDetail.setTotalAmount(statisticsDetail.getStatisticsCategories().stream().map(
                        StatisticsCategory::getProductBasedTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        List<StatisticsCategory> statisticsCategories = addPercentage(statisticsDetail);
        statisticsDetail.setStatisticsCategories(statisticsCategories);
        return statisticsDetail;

    }

    private StatisticsDetail getMonthlyData(LocalDateTime startDate, LocalDateTime endDate) {
        StatisticsDetail statisticsDetail = StatisticsDetail.builder()
                .statisticsCategories(
                        productRepository.getAllProductTypes(getUser().getId(), startDate, endDate).stream().map(productType -> {
                            List<ChartSingleResponse> statisticsDetailsByMonth =
                                    productRepository.getStatisticsDetailsByMonth(ProductType.valueOf(productType),
                                            getUser().getId());
                            return getStatisticsCategoryResponse(statisticsDetailsByMonth, productType);
                        }).collect(Collectors.toList()))
                .range(DateRange.MONTHLY)
                .build();

        statisticsDetail.setTotalAmount(statisticsDetail.getStatisticsCategories().stream().map(
                        StatisticsCategory::getProductBasedTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        List<StatisticsCategory> statisticsCategories = addPercentage(statisticsDetail);

        statisticsDetail.setStatisticsCategories(statisticsCategories);
        return statisticsDetail;

    }

    private StatisticsDetail getDailyData(LocalDateTime startDate, LocalDateTime endDate) {
        StatisticsDetail statisticsDetail = StatisticsDetail.builder()
                .statisticsCategories(
                        productRepository.getAllProductTypes(getUser().getId(), startDate, endDate).stream().map(productType -> {
                            List<ChartSingleResponse> statisticsDetailsByMonth =
                                    productRepository.getStatisticsDetailsByDay(ProductType.valueOf(productType),
                                            getUser().getId());
                            return getStatisticsCategoryResponse(statisticsDetailsByMonth, productType);
                        }).collect(Collectors.toList()))
                .range(DateRange.DAILY)
                .build();

        statisticsDetail.setTotalAmount(statisticsDetail.getStatisticsCategories().stream().map(
                        StatisticsCategory::getProductBasedTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));


        List<StatisticsCategory> statisticsCategories = addPercentage(statisticsDetail);

        statisticsDetail.setStatisticsCategories(statisticsCategories);
        return statisticsDetail;

    }

    private List<StatisticsCategory> addPercentage(StatisticsDetail statisticsDetail) {
        return statisticsDetail.getStatisticsCategories().stream().map(category -> {
            category.setPercentage(
                    category.getProductBasedTotalPrice()
                            .divide(statisticsDetail.getTotalAmount(), RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)));
            return category;
        }).collect(Collectors.toList());
    }

    private StatisticsCategory getStatisticsCategoryResponse(List<ChartSingleResponse> chartValues,
                                                             String productType) {
        return StatisticsCategory.builder()
                .productBasedTotalPrice(chartValues.stream()
                        .map(ChartSingleResponse::getAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .chartDetails(chartValues)
                .productType(productType)
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
