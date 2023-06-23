package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.request.ProductRequest;
import com.pullm.backendmonolit.models.response.ChartResponse;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {
    List<ChartResponse> getAllChartResponse(LocalDate startDate, LocalDate endDate);
    void updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long id);
}
