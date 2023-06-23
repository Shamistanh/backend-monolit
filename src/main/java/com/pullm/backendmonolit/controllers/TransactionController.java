package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.models.request.TransactionRequest;
import com.pullm.backendmonolit.models.response.TransactionResponse;
import com.pullm.backendmonolit.services.TransactionsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/transactions")
public class TransactionController {

    private final TransactionsService transactionsService;

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
    public List<TransactionResponse> getAllTransactions() {
        return transactionsService.getAllTransactions();
    }
}
