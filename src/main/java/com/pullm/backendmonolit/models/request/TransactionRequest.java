package com.pullm.backendmonolit.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TransactionRequest {
    private String storeName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime date;
    private String currency;
    private List<ProductRequest> products;

    public TransactionRequest() {
        this.currency = "AZN";
    }

}
