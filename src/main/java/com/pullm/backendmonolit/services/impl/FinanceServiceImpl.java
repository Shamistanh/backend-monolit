package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.entities.UserFinance;
import com.pullm.backendmonolit.enums.FinancialType;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.request.FinancialStatusRequest;
import com.pullm.backendmonolit.models.response.FinancialStatusResponse;
import com.pullm.backendmonolit.repository.UserFinancialConditionsRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.FinanceService;
import java.math.BigDecimal;
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

    private final UserFinancialConditionsRepository financialConditionsRepository;

    @Override
    public FinancialStatusResponse getFinancialCondition() {
        User user = getUser();
        Optional<UserFinance> byUserId = financialConditionsRepository.findByUserId(getUser().getId());

        if (byUserId.isPresent()) {
            UserFinance userFinance = byUserId.get();
            return getFinancialStatusResponse(userFinance);
        }
        return FinancialStatusResponse.builder()
                .userId(user.getId())
                .balance(BigDecimal.ZERO)
                .monthlyIncome(BigDecimal.ZERO)
                .monthlyExpense(BigDecimal.ZERO)
                .build();
    }

    @Override
    public Boolean changeAmount(FinancialStatusRequest financialStatusRequest) {
        try {
            User user = getUser();
            Optional<UserFinance> financialConditionsOptional =
                    financialConditionsRepository.findByUserId(user.getId());

            if (financialConditionsOptional.isPresent()) {
                UserFinance userFinance = financialConditionsOptional.get();
                if (financialStatusRequest.getFinancialType() == FinancialType.INCOME) {
                    userFinance.setMonthlyIncome(
                            userFinance.getMonthlyIncome().add(financialStatusRequest.getAmount()));
                    userFinance.setBalance(
                            userFinance.getBalance().add(financialStatusRequest.getAmount()));
                } else {
                    userFinance.setMonthlyExpense(
                            userFinance.getMonthlyExpense().add(financialStatusRequest.getAmount()));
                    userFinance.setBalance(
                            userFinance.getBalance().subtract(financialStatusRequest.getAmount()));
                }
                financialConditionsRepository.save(userFinance);
                return true;
            }

            return initiateFinancialOvewview(financialStatusRequest, user);


        }catch (Exception e) {
            log.error("Error while changing financial status", e);
            return false;
        }
    }

    private Boolean initiateFinancialOvewview(FinancialStatusRequest financialStatusRequest, User user) {
        if (financialStatusRequest.getFinancialType() == FinancialType.INCOME) {
            financialConditionsRepository.save(UserFinance.builder()
                    .user(user)
                    .balance(financialStatusRequest.getAmount())
                    .monthlyExpense(BigDecimal.ZERO)
                    .monthlyIncome(financialStatusRequest.getAmount())
                    .build());
        } else {
            financialConditionsRepository.save(UserFinance.builder()
                    .user(user)
                    .monthlyExpense(financialStatusRequest.getAmount())
                    .monthlyIncome(BigDecimal.ZERO)
                    .balance(financialStatusRequest.getAmount().negate())
                    .build());
        }


        return true;
    }

    private FinancialStatusResponse getFinancialStatusResponse(UserFinance userFinance) {
        return FinancialStatusResponse.builder()
                .balance(userFinance.getBalance())
                .monthlyExpense(userFinance.getMonthlyExpense())
                .monthlyIncome(userFinance.getMonthlyIncome())
                .build();
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
