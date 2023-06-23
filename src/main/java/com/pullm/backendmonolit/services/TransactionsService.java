package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.request.TransactionRequest;
import com.pullm.backendmonolit.models.response.ChartResponse;
import com.pullm.backendmonolit.models.response.TransactionResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionsService {

    void createTransaction(TransactionRequest transactionRequest);
    void updateTransaction(Long id, TransactionRequest transactionRequest);
    List<TransactionResponse> getAllTransactions();

}
