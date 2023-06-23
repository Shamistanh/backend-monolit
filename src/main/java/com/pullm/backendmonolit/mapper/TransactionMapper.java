package com.pullm.backendmonolit.mapper;

import com.pullm.backendmonolit.entities.Transaction;
import com.pullm.backendmonolit.models.request.TransactionRequest;
import com.pullm.backendmonolit.models.response.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    Transaction mapToTransactionEntity(TransactionRequest transactionRequest);

    List<TransactionResponse> mapToTransactionResonseList(List<Transaction> transactions);
}
