package com.pullm.backendmonolit.client;

import com.pullm.backendmonolit.models.response.ExchangeRateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "exchangeRateClient", url = "https://open.er-api.com/v6/latest")
public interface ExchangeRateClient {

    @GetMapping("/AZN")
    ExchangeRateResponse getExchangeRates();
}
