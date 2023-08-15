package com.pullm.backendmonolit.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<TransactionDetailsResponse> transactionDetails;
}
