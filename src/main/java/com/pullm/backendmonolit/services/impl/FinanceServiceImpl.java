package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.Transaction;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.entities.UserIncome;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.request.AddIncomeRequest;
import com.pullm.backendmonolit.models.response.FinancialStatusResponse;
import com.pullm.backendmonolit.repository.TransactionRepository;
import com.pullm.backendmonolit.repository.UserIncomeRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.FinanceService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private final UserIncomeRepository userIncomeRepository;

    @Override
    public FinancialStatusResponse getFinancialCondition() {

        BigDecimal monthlyIncomeOfUser = findMonthlyIncomeOfUser();
        BigDecimal monthlyExpenseOfUser = findMonthlyExpenseOfUser();
        BigDecimal balance = monthlyIncomeOfUser.subtract(monthlyExpenseOfUser);
        return FinancialStatusResponse.builder()
                .userId(getUser().getId())
                .balance(balance)
                .monthlyIncome(monthlyIncomeOfUser)
                .monthlyExpense(monthlyExpenseOfUser)
                .build();
    }

    @Override
    public Boolean addIncome(AddIncomeRequest addIncomeRequest) {
        try {
            User user = getUser();

            userIncomeRepository.save(UserIncome.builder()
                    .date(addIncomeRequest.getDate().atStartOfDay())
                    .incomeType(addIncomeRequest.getIncomeType())
                    .amount(addIncomeRequest.getAmount())
                    .user(user)
                    .build());
            return true;


        } catch (Exception e) {
            log.error("Error while adding income", e);
            return false;
        }
    }


    private BigDecimal findMonthlyExpenseOfUser() {
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);
        return transactionRepository.findAllByUserIdAndDateBetween(getUser().getId(),
                        startOfMonth, endOfMonth).stream().map(Transaction::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }


    private BigDecimal findMonthlyIncomeOfUser() {
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);
        return userIncomeRepository.findAllByUserIdAndDateBetween(getUser().getId(),
                        startOfMonth, endOfMonth).stream().map(UserIncome::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private User getUser() {
        var number = extractMobileNumber();
        var user = userRepository.findUserByEmail(number)
                .orElseThrow(() -> new NotFoundException("Phone number not found"));

        log.info("getUser(): user-id: " + user.getId());

        return user;
    }

    private String extractMobileNumber() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

}
