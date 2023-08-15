package com.pullm.backendmonolit.mapper;

import com.pullm.backendmonolit.entities.Transaction;
import com.pullm.backendmonolit.models.request.TransactionRequest;
import com.pullm.backendmonolit.models.response.ProductResponse;
import com.pullm.backendmonolit.models.response.TransactionDetailsResponse;
import com.pullm.backendmonolit.models.response.TransactionResponse;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    Transaction mapToTransactionEntity(TransactionRequest transactionRequest);

    default List<TransactionResponse> mapToTransactionResponseList(List<Transaction> transactions) {
        var transactionsByDateAndStore = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.groupingBy(transaction -> transaction.getDate().toLocalDate(),
                        Collectors.groupingBy(Transaction::getStoreName)));

        return transactionsByDateAndStore.entrySet().stream()
                .map(dateStoreEntry -> {
                    var date = dateStoreEntry.getKey();
                    var storeTransactionsMap = dateStoreEntry.getValue();

                    var transactionResponse = new TransactionResponse();
                    transactionResponse.setDate(date);

                    var transactionDetailsRequests = storeTransactionsMap.entrySet().stream()
                            .map(storeEntry -> {
                                var storeName = storeEntry.getKey();
                                var storeTransactions = storeEntry.getValue();

                                var transactionDetailsResponse = new TransactionDetailsResponse();
                                transactionDetailsResponse.setStoreName(storeName);

                                var totalAmount = storeTransactions.stream()
                                        .map(Transaction::getTotalAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                                transactionDetailsResponse.setTotalPrice(totalAmount);

                                var productCount = storeTransactions.stream()
                                        .mapToInt(transaction -> transaction.getProducts().size())
                                        .sum();

                                transactionDetailsResponse.setProductCount(productCount);

                                storeTransactions.stream().findAny().ifPresent(transaction ->
                                        transactionDetailsResponse.setDateTime(transaction.getDate()));

                                var productRequests = storeTransactions.stream()
                                        .flatMap(transaction -> transaction.getProducts().stream())
                                        .map(product -> {
                                            var productResponse = new ProductResponse();
                                            productResponse.setName(product.getName());
                                            productResponse.setPrice(product.getPrice());
                                            productResponse.setQuantity(
                                                    getQuantity(product.getWeight(), product.getCount()));
                                            return productResponse;
                                        }).collect(Collectors.toList());

                                transactionDetailsResponse.setProducts(productRequests);
                                return transactionDetailsResponse;
                            }).collect(Collectors.toList());

                    transactionResponse.setTransactionDetails(transactionDetailsRequests);
                    return transactionResponse;
                }).collect(Collectors.toList());
    }

    private BigDecimal getQuantity(BigDecimal weight, Integer count) {
        if (count == null || count.equals(0)) {
            return weight;
        }
        return BigDecimal.valueOf(count);
    }
}
