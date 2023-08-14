package com.pullm.backendmonolit.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<TransactionDetailsResponse> transactionDetails;
}
