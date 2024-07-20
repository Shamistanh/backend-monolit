package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.models.criteria.DateCriteria;
import com.pullm.backendmonolit.models.request.ProductRequest;
import com.pullm.backendmonolit.models.response.ChartResponse;
import com.pullm.backendmonolit.models.response.PopularProductResponse;
import com.pullm.backendmonolit.services.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/products")
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping("/chart")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public List<ChartResponse> getAllChartResponse(DateCriteria dateCriteria) {
        return productService.getAllChartResponse(dateCriteria);
    }

    @GetMapping("/popular/{limit}")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public List<PopularProductResponse> getPopularProducts(@PathVariable("limit") int limit) {
        return productService.getPopularProducts(limit);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer Authentication")
    void updateProduct(Long id, ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer Authentication")
    void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }

}
