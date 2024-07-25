package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.request.CurrencyRequest;
import com.pullm.backendmonolit.models.response.CurrencyResponse;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.util.Pair;

public interface ConversionService {

    double convertAmount(double amount);

    List<CurrencyResponse> getAvailableCurrencies();

    Boolean setCurrency(CurrencyRequest currencyRequest);

    Pair<String, Double> getCurrentCurrency();

}
