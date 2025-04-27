package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.models.request.CurrencyRequest;
import com.pullm.backendmonolit.models.response.CurrencyResponse;
import com.pullm.backendmonolit.services.ConversionService;
import com.pullm.backendmonolit.services.impl.ConversionServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/currency")
public class CurrencyController {

    private final ConversionService conversionService;

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CurrencyResponse> getAvailableCurrencies() {
        return conversionService.getAvailableCurrencies();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public Boolean setAvailableCurrencies(@RequestBody CurrencyRequest currencyRequest) {
        return conversionService.setCurrency(currencyRequest);
    }

}
