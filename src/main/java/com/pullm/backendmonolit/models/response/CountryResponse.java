package com.pullm.backendmonolit.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryResponse {
    @JsonProperty("flags")
    private Flags flags;

    private Map<String, CurrencyDetail> currencies;
}
