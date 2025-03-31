package com.pullm.backendmonolit.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ProcessedReceipt {

    @JsonProperty("object_name")
    private String objectName;

    private String date;
    private String time;
    private List<Product> products;

    @JsonProperty("paymentType")
    private PaymentType paymentType;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("total_tax")
    private BigDecimal totalTax;

    @Data
    public static class Product {

        private String product;

        @JsonProperty("product_type")
        private String productType;

        private int quantity;

        @JsonProperty("product_measurement")
        private String productMeasurement;

        private BigDecimal price;
        private BigDecimal total;

    }

    @Data
    public static class PaymentType {

        private BigDecimal cashless;
        private BigDecimal cash;
        private BigDecimal bonus;
        private BigDecimal prepayment;
        private BigDecimal credit;

    }

}

