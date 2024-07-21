package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.client.CountryClient;
import com.pullm.backendmonolit.client.ExchangeRateClient;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.request.CurrencyRequest;
import com.pullm.backendmonolit.models.response.CurrencyResponse;
import com.pullm.backendmonolit.models.response.ExchangeRateResponse;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.ConversionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversionServiceImpl implements ConversionService {

    private final ExchangeRateClient exchangeRateClient;

    private final CountryClient countryClient;

    private final UserRepository userRepository;

    private static String AZN = "AZN";

    public double convertAmount(double amount) {
        Double rate = exchangeRateClient.getExchangeRates().getRates().get(getUser().getCurrency() == null ? AZN :
                getUser().getCurrency());
        return amount * rate;
    }

    public List<CurrencyResponse> getAvailableCurrencies() {
        List<CurrencyResponse> currencies = new ArrayList<>();
        exchangeRateClient.getExchangeRates().getRates().forEach((k, v) -> currencies.add(CurrencyResponse.builder()
                        .flag(getCountryFlag(k))
                .currencyCode(k)
                .rate(v)
                .build()));
        return currencies;
    }

    private String getCountryFlag(String k) {
        try {
            return countryClient.getCountry(k).get(0).getFlags().getPng();
        }catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Boolean setCurrency(CurrencyRequest currencyRequest) {
        User user = getUser();
        user.setCurrency(currencyRequest.getCurrency());
        userRepository.save(user);
        return true;
    }

    private User getUser() {
        var number = extractMobileNumber();
        var user = userRepository.findUserByEmail(number)
                .orElseThrow(() -> new NotFoundException("Phone number not found"));

        log.info("getUser(): user-id: " + user.getId());

        return user;
    }

    private String extractMobileNumber() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

}
