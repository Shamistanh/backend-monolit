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
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;
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
    private final ConversionServiceImpl conversionServiceImpl;

    @Override
    public FinancialStatusResponse getFinancialCondition() {

        BigDecimal monthlyIncomeOfUser = findMonthlyIncomeOfUser();
        BigDecimal monthlyExpenseOfUser = findMonthlyExpenseOfUser();
        BigDecimal balance = monthlyIncomeOfUser.subtract(monthlyExpenseOfUser);
        Pair<String, Double> currentCurrency = conversionServiceImpl.getCurrentCurrency();
        BigDecimal convertedIncome = monthlyIncomeOfUser.divide(BigDecimal.valueOf(currentCurrency.getSecond()),
                2, RoundingMode.HALF_UP);

        BigDecimal convertedExpense = monthlyExpenseOfUser.divide(BigDecimal.valueOf(currentCurrency.getSecond()),
                2, RoundingMode.HALF_UP);

        return FinancialStatusResponse.builder()
                .userId(getUser().getId())
                .balance(balance)
                .currency(currentCurrency.getFirst())
                .monthlyIncome(convertedIncome.setScale(2, RoundingMode.HALF_UP))
                .monthlyExpense(convertedExpense.setScale(2, RoundingMode.HALF_UP))
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
                    .currency(addIncomeRequest.getCurrency())
                    .user(user)
                    .build());
            return true;


        } catch (Exception e) {
            log.error("Error while adding income", e);
            return false;
        }
    }

    @Override
    public Boolean deleteIncome(Long id) {
        try {
            userIncomeRepository.deleteByIdAndUser(id, getUser());
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }

    }


    private BigDecimal findMonthlyExpenseOfUser() {
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);
        return transactionRepository.findAllByUserIdAndDateBetween(getUser().getId(),
                        startOfMonth, endOfMonth).stream().map(this:: convertAndReturnAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }


    private BigDecimal findMonthlyIncomeOfUser() {
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);
        return userIncomeRepository.findAllByUserIdAndDateBetween(getUser().getId(),
                        startOfMonth, endOfMonth).stream().map(this::convertAndReturnAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private BigDecimal convertAndReturnAmount(UserIncome userIncome) {
        BigDecimal amount = userIncome.getAmount();
        String currency = userIncome.getCurrency();
        return BigDecimal.valueOf(conversionServiceImpl.convertAmount(amount.doubleValue(), currency));
    }

    private BigDecimal convertAndReturnAmount(Transaction transaction) {
        BigDecimal amount = transaction.getTotalAmount();
        String currency = transaction.getCurrency();
        return BigDecimal.valueOf(conversionServiceImpl
                .convertAmount(amount.multiply(BigDecimal.valueOf(transaction.getRate())).doubleValue(), currency));
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
