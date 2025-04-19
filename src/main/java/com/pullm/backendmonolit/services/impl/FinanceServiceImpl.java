package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.Transaction;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.entities.UserIncome;
import com.pullm.backendmonolit.enums.FinanceRange;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.request.AddIncomeRequest;
import com.pullm.backendmonolit.models.response.FinancialStatusOverallResponse;
import com.pullm.backendmonolit.models.response.FinancialStatusResponse;
import com.pullm.backendmonolit.repository.TransactionRepository;
import com.pullm.backendmonolit.repository.UserIncomeRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.FinanceService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
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

        return FinancialStatusResponse.builder()
                .userId(getUser().getId())
                .balance(balance)
                .currency(currentCurrency.getFirst())
                .monthlyIncome(monthlyIncomeOfUser.setScale(2, RoundingMode.HALF_UP))
                .monthlyExpense(monthlyExpenseOfUser.setScale(2, RoundingMode.HALF_UP))
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
                    .rate(conversionServiceImpl.getConversionRateByCurrencyCode(addIncomeRequest.getCurrency()))
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

    @Override
    public FinancialStatusOverallResponse getFinancialCondition(FinanceRange financeRange, LocalDateTime startDate,
                                                                LocalDateTime endDate) {
        BigDecimal monthlyIncomeOfUser = findMonthlyIncomeOfUser(financeRange, startDate, endDate);
        BigDecimal monthlyExpenseOfUser = findMonthlyExpenseOfUser(financeRange, startDate, endDate);
        BigDecimal balance = monthlyIncomeOfUser.subtract(monthlyExpenseOfUser);
        Pair<String, Double> currentCurrency = conversionServiceImpl.getCurrentCurrency();

        return FinancialStatusOverallResponse.builder()
                .userId(getUser().getId())
                .balance(balance)
                .currency(currentCurrency.getFirst())
                .income(monthlyIncomeOfUser.setScale(2, RoundingMode.HALF_UP))
                .expense(monthlyExpenseOfUser.setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    private BigDecimal findMonthlyExpenseOfUser(FinanceRange financeRange, LocalDateTime startDate,
                                                LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
            return transactionRepository.findAllByUserIdAndDateBetween(getUser().getId(),
                            startDate, endDate).stream().map(this::convertAndReturnAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (financeRange == FinanceRange.TOTAL) {
            return transactionRepository.findAllByUserId(getUser().getId())
                    .stream().map(this::convertAndReturnAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return findMonthlyExpenseOfUser();
    }


    private BigDecimal findMonthlyIncomeOfUser(FinanceRange financeRange,
                                               LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime start = startDate;
        LocalDateTime end = endDate;

        if (start == null || end == null) {
            if (financeRange == null) {
                throw new IllegalArgumentException("All required parameters are null");
            }
            if (financeRange == FinanceRange.CURRENT_MONTH) {
                YearMonth currentMonth = YearMonth.now();
                start = currentMonth.atDay(1).atStartOfDay();
                end = currentMonth.atEndOfMonth().atTime(23, 59, 59);
            } else if (financeRange == FinanceRange.TOTAL) {
                // Fetch all without date filter
                return sumIncome(userIncomeRepository.findAllByUserId(getUser().getId()));
            }
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return sumIncome(userIncomeRepository.findAllByUserIdAndDateBetween(getUser().getId(), start, end));
    }

    private BigDecimal sumIncome(List<UserIncome> incomes) {
        return incomes.stream()
                .map(income -> income.getAmount().divide(
                        income.getRate() != null ? income.getRate() : BigDecimal.ONE,
                        2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
                        startOfMonth, endOfMonth).stream().map(income->
                        income.getAmount().divide(income.getRate() !=null ? income.getRate()
                                        : BigDecimal.ONE, 2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private BigDecimal convertAndReturnAmount(Transaction transaction) {
        BigDecimal amount = transaction.getTotalAmount();
        return BigDecimal.valueOf(amount.divide(BigDecimal
                .valueOf(transaction.getRate() !=null ? transaction.getRate()
                        : 1d),RoundingMode.HALF_UP).doubleValue());
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
