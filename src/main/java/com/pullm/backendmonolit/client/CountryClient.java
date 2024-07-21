package com.pullm.backendmonolit.client;

import com.pullm.backendmonolit.models.response.CountryResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "countryClient", url = "https://restcountries.com/v3.1/currency")
public interface CountryClient {

    @GetMapping("/{currencyCode}")
    List<CountryResponse> getCountry(@PathVariable("currencyCode")String currencyCode);
}
