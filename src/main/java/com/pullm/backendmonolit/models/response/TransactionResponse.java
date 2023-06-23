package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.entities.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private String storeName;
    private BigDecimal totalAmount;
    private LocalDate date;
    private TransactionType transactionType;
    private List<ProductResponse> products;
}
