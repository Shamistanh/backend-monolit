package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.Product;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.entities.enums.ProductType;
import com.pullm.backendmonolit.entities.enums.TransactionType;
import com.pullm.backendmonolit.exception.ResourceNotFoundException;
import com.pullm.backendmonolit.mapper.TransactionMapper;
import com.pullm.backendmonolit.models.request.TransactionRequest;
import com.pullm.backendmonolit.models.response.TransactionResponse;
import com.pullm.backendmonolit.repository.TransactionRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.TransactionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.pullm.backendmonolit.consatnts.Constants.findProductTypeBySubType;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionMapper transactionMapper = TransactionMapper.INSTANCE;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(isolation = Isolation.DEFAULT)
    public void createTransaction(TransactionRequest transactionRequest) {
        log.info("createTransaction().start");

        var transaction = transactionMapper.mapToTransactionEntity(transactionRequest);

        var totalAmount = transaction.getProducts()
                .stream()
                .map(Product::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var user = getUser();
        transaction.setUser(user);
        transaction.setTransactionType(TransactionType.MANUAL);
        transaction.setTotalAmount(totalAmount);

        transaction.getProducts().forEach(product -> {
            ProductType productType = findProductTypeBySubType(product.getProductSubType());
            product.setProductType(productType);
            product.setTransaction(transaction);
        });

        transactionRepository.save(transaction);

        log.info("createTransaction().end with transaction id: " + transaction.getId());
    }

    @Override
    public void updateTransaction(Long id, TransactionRequest transactionRequest) {
        log.info("updateTransaction().start");

        var transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found by id " + id));

        if (transactionRequest.getStoreName() != null) {
            transaction.setStoreName(transactionRequest.getStoreName());
        }

        if (transactionRequest.getDate() != null) {
            transaction.setDate(transactionRequest.getDate());
        }

        transactionRepository.save(transaction);

        log.info("updateTransaction().end with transaction id: " + transaction.getId());
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        log.info("getAllTransactions().start");

        User user = getUser();

        var transactions = transactionRepository.findAllByUserId(user.getId());

        var transactionRequests = transactionMapper.mapToTransactionResonseList(transactions);

        log.info("getAllTransactions().end");

        return transactionRequests;
    }

    private User getUser() {
        var number = extractMobileNumber();
        var user = userRepository.findUserByEmail(number)
                .orElseThrow(() -> new UsernameNotFoundException("Phone number not found"));

        log.info("getUser(): user-id: " + user.getId());

        return user;
    }

    private String extractMobileNumber() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }
}
