package com.pullm.backendmonolit.services.impl;

import static com.pullm.backendmonolit.consatnts.Constants.findProductTypeBySubType;

import com.pullm.backendmonolit.entities.Product;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.entities.enums.TransactionType;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.mapper.TransactionMapper;
import com.pullm.backendmonolit.models.criteria.DateCriteria;
import com.pullm.backendmonolit.models.request.TransactionRequest;
import com.pullm.backendmonolit.models.response.TransactionResponse;
import com.pullm.backendmonolit.repository.TransactionRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.ConversionService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl {

    private final TransactionMapper transactionMapper = TransactionMapper.INSTANCE;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ConversionService conversionService;

    @Transactional(isolation = Isolation.DEFAULT)
    public void createTransaction(TransactionRequest transactionRequest) {
        log.info("createTransaction().start");

        var transaction = transactionMapper.mapToTransactionEntity(transactionRequest);

        var totalAmount = transaction.getProducts()
                .stream()
                .map(Product::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pair<String, Double> currentCurrency = conversionService.getCurrentCurrency();
        var user = getUser();
        transaction.setUser(user);
        transaction.setTransactionType(TransactionType.MANUAL);
        transaction.setTotalAmount(
                totalAmount.divide(BigDecimal.valueOf(currentCurrency.getSecond()), RoundingMode.HALF_UP));
        transaction.setRate(currentCurrency.getSecond());
        transaction.setCurrency(currentCurrency.getFirst());

        transaction.getProducts().forEach(product -> {
            ProductType productType = findProductTypeBySubType(product.getProductSubType());
            product.setProductType(productType);
            product.setPrice(
                    product.getPrice().divide(BigDecimal.valueOf(currentCurrency.getSecond()), RoundingMode.HALF_UP));
            product.setTransaction(transaction);
        });

        transactionRepository.save(transaction);

        log.info("createTransaction().end with transaction id: " + transaction.getId());
    }

    public void updateTransaction(Long id, TransactionRequest transactionRequest) {
        log.info("updateTransaction().start");

        var transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found by id " + id));

        if (transactionRequest.getStoreName() != null) {
            transaction.setStoreName(transactionRequest.getStoreName());
        }

        if (transactionRequest.getDate() != null) {
            transaction.setDate(transactionRequest.getDate());
        }

        transactionRepository.save(transaction);

        log.info("updateTransaction().end with transaction id: " + transaction.getId());
    }

    public List<TransactionResponse> getAllTransactions(DateCriteria dateCriteria) {
        log.info("getAllTransactions().start DateCriteria: {}", dateCriteria);

        User user = getUser();
        LocalDateTime fromDate = dateCriteria.getFromDate().atStartOfDay();
        LocalDateTime toDate = dateCriteria.getToDate().atStartOfDay();

        var transactions = transactionRepository.findAllByUserIdAndDateBetween(user.getId(), fromDate, toDate);
        var transactionRequests = transactionMapper.mapToTransactionResponseList(transactions);

        log.info("getAllTransactions().end");

        return transactionRequests;
    }

    private User getUser() {
        var email = extractEmail();
        var user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email not found"));

        log.info("getUser(): user-id: " + user.getId());

        return user;
    }

    private String extractEmail() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }
}
