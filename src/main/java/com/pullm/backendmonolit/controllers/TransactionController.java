package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.models.criteria.DateCriteria;
import com.pullm.backendmonolit.models.request.TransactionRequest;
import com.pullm.backendmonolit.models.response.ResponseDTO;
import com.pullm.backendmonolit.models.response.TransactionResponse;
import com.pullm.backendmonolit.services.impl.TransactionsServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/transactions")
public class TransactionController {

    private final TransactionsServiceImpl transactionsService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @SecurityRequirement(name = "Bearer Authentication")
    public void createTransaction(@RequestBody TransactionRequest transactionRequest) {
        transactionsService.createTransaction(transactionRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer Authentication")
    public void updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) {
        transactionsService.updateTransaction(id, transactionRequest);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<List<TransactionResponse>> getAllTransactions(DateCriteria dateCriteria) {
        return ResponseDTO.<List<TransactionResponse>>builder()
                .data(transactionsService.getAllTransactions(dateCriteria)).build();
    }
}
