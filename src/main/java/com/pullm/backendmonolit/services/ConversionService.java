package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.request.CurrencyRequest;
import java.util.Set;

public interface ConversionService {

    double convertAmount(double amount);

    Set<String> getAvailableCurrencies();

    Boolean setCurrency(CurrencyRequest currencyRequest);

}
