package com.pullm.backendmonolit.models.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class Receipt {
    private String date;
    private String objectName;
    private PaymentType paymentType;
    private List<Product> productsPurchased;
    private String time;
    private Total total;

    @Data
    @NoArgsConstructor
    public static class PaymentType {
        private BigDecimal bonus;
        private BigDecimal cash;
        private BigDecimal cashless;
        private BigDecimal credit;
        private BigDecimal prepayment;
    }

    @Data
    @NoArgsConstructor
    public static class Product {
        private BigDecimal price;
        private String productName;
        private BigDecimal quantity;
        private BigDecimal totalPrice;
    }

    @Data
    @NoArgsConstructor
    public static class Total {
        private BigDecimal totalAmount;
        private BigDecimal totalTax;
        private BigDecimal vat18;
    }
}