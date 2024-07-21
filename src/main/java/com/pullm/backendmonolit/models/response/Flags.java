package com.pullm.backendmonolit.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Flags {
    @JsonProperty("png")
    private String png;
    @JsonProperty("svg")
    private String svg;
    @JsonProperty("alt")
    private String alt;
}