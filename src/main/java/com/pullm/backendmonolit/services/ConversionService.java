package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.request.CurrencyRequest;
import com.pullm.backendmonolit.models.response.CurrencyResponse;
import java.util.List;

public interface ConversionService {

    double convertAmount(double amount);

    List<CurrencyResponse> getAvailableCurrencies();

    Boolean setCurrency(CurrencyRequest currencyRequest);

}
