package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.models.request.ProductRequest;
import com.pullm.backendmonolit.models.response.ChartResponse;
import com.pullm.backendmonolit.services.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/chart")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public List<ChartResponse> getAllChartResponse(LocalDate startDate, LocalDate endDate) {
        return productService.getAllChartResponse(startDate, endDate);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer Authentication")
    void updateProduct(Long id, ProductRequest productRequest){
        productService.updateProduct(id, productRequest);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer Authentication")
    void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }

}
