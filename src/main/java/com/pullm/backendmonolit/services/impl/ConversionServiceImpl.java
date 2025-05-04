package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.client.CountryClient;
import com.pullm.backendmonolit.client.ExchangeRateClient;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.request.CurrencyRequest;
import com.pullm.backendmonolit.models.response.CountryResponse;
import com.pullm.backendmonolit.models.response.CurrencyResponse;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.ConversionService;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Pair;
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

    private final HttpServletRequest request;

    private static final String AZN = "AZN";
    private static final String USD = "USD";
    private static final String EUR = "EUR";
    private static final String TRY = "TRY";
    private static final String RUB = "TRY";
    private static final String GBP = "GBP";
    public List<String> CURRENCIES = List.of(AZN, USD, EUR, TRY, RUB, GBP);

    private static final String ACCEPT_CURRENCY = "accept-currency";

    public double convertAmount(double amount) {
        Double rate = exchangeRateClient.getExchangeRates().getRates().get(getCurrency());
        if (rate == null) {
            rate = 1d;
        }
        BigDecimal bd = new BigDecimal(Double.toString(rate * amount));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double convertAmount(double amount, String currency) {
        try {
            Double rate = exchangeRateClient.getExchangeRates().getRates().get(currency);
            BigDecimal bd = new BigDecimal(Double.toString( amount / rate));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } catch (Exception e) {
            log.error(e.getMessage());
            return amount;
        }
    }

    public BigDecimal getConversionRateByCurrencyCode(String currency) {
        return BigDecimal.valueOf(getAvailableCurrencies().stream()
                .filter(currencyResponse -> currency.equals(currencyResponse.getCurrencyCode()))
                .findAny().orElse(CurrencyResponse.builder().rate(0d).build()).getRate());
    }

    @Cacheable("currencies")
    public List<CurrencyResponse> getAvailableCurrencies() {
        log.info("Calling getAvailableCurrencies");
        List<CurrencyResponse> currencies = new ArrayList<>();
        exchangeRateClient.getExchangeRates().getRates().forEach((k, v) -> {
                    if (CURRENCIES.contains(k)) {
                        Pair<String, String> countryDetails = getCountryDetails(k);
                        currencies.add(CurrencyResponse.builder()
                                .flag(countryDetails != null ? countryDetails.getFirst() : "")
                                .currencyCode(k)
                                .symbol(countryDetails != null ? countryDetails.getSecond() : "")
                                .rate(v)
                                .build());
                    }
                }

        );
        return currencies;
    }

    private Pair<String, String> getCountryDetails(String k) {
        try {
            List<CountryResponse> country = countryClient.getCountry(k);
            return Pair.of(country.get(0).getFlags().getPng(), country.get(0).getCurrencies().get(k).getSymbol());
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private String getCountryFlag(String k) {
        try {
            return countryClient.getCountry(k).get(0).getFlags().getPng();
        }catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private String getCurrency() {
        try {
            Optional<User> userByEmail = userRepository.findUserByEmail(extractMobileNumber());
            return request.getHeader(ACCEPT_CURRENCY) == null ?
                    userByEmail.map(User::getCurrency).orElse(AZN) :
                    request.getHeader(ACCEPT_CURRENCY);
        } catch (Exception e) {
            log.error(e.getMessage());
            return AZN;
        }
    }

    public Boolean setCurrency(CurrencyRequest currencyRequest) {
        User user = getUser();
        user.setCurrency(currencyRequest.getCurrency());
        userRepository.save(user);
        return true;
    }

    @Override
    public Pair<String, Double> getCurrentCurrency() {
        return Pair.of(getCurrency(), exchangeRateClient.getExchangeRates().getRates().get(getCurrency()));
    }

    @Override
    public Pair<String, Double> getCurrency(String currency) {
        return Pair.of(getCurrency(), exchangeRateClient.getExchangeRates().getRates().get(currency));
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
