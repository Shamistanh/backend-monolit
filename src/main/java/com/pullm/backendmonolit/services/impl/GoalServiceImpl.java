package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.Goal;
import com.pullm.backendmonolit.entities.Transaction;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.enums.GoalStatus;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.request.GoalRequest;
import com.pullm.backendmonolit.models.response.GoalResponse;
import com.pullm.backendmonolit.models.response.GoalSingleResponse;
import com.pullm.backendmonolit.repository.GoalRepository;
import com.pullm.backendmonolit.repository.TransactionRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.GoalService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    @Override
    public GoalSingleResponse getGoal(Long id) {
        User user = getUser();
        Optional<Goal> goalRepositoryById = goalRepository.findByIdAndUser(id, user);

        BigDecimal monthlyExpenseOfUser = findMonthlyExpenseOfUser();

        //TODO will be changed after finance update
        BigDecimal monthlyIncome = BigDecimal.valueOf(300);
        if (goalRepositoryById.isPresent()) {

            Goal goal = goalRepositoryById.get();

            BigDecimal balance = monthlyIncome.subtract(monthlyExpenseOfUser);

            GoalSingleResponse goalSingleResponse = GoalSingleResponse.builder()
                    .startDate(goal.getStartDate())
                    .endDate(goal.getEndDate())
                    .name(goal.getName())
                    .amount(goal.getAmount())
                    .monthlyTotalExpenses(monthlyExpenseOfUser)
                    .balance(balance)
                    .status(goal.getStatus())
                    .build();

            if (balance.compareTo(goal.getAmount()) >= 0) {
                goalSingleResponse.setPercentage(100);
            }else if(balance.compareTo(BigDecimal.ZERO) <= 0){
                goalSingleResponse.setPercentage(0);
            }else {
                goalSingleResponse.setPercentage(Math.round(balance.doubleValue() / goal.getAmount().doubleValue() * 100.0));
            }

            return goalSingleResponse;
        }
        return null;
    }

    @Override
    public GoalResponse getAllGoals(GoalStatus status) {
        User user = getUser();
        List<Goal> goals;

        if (status != null) {
            goals = goalRepository.findAllByUserAndStatus(user, status);
        }else {
            goals = goalRepository.findAllByUser(user);
        }


        List<GoalSingleResponse> goalSingleResponses = new ArrayList<>();
        goals.forEach(goal -> {
            BigDecimal monthlyExpenseOfUser = findMonthlyExpenseOfUser();

            //TODO will be changed after finance update
            BigDecimal monthlyIncome = BigDecimal.valueOf(300);

            BigDecimal balance = monthlyIncome.subtract(monthlyExpenseOfUser);

            GoalSingleResponse goalSingleResponse = GoalSingleResponse.builder()
                    .startDate(goal.getStartDate())
                    .endDate(goal.getEndDate())
                    .name(goal.getName())
                    .amount(goal.getAmount())
                    .monthlyTotalExpenses(monthlyExpenseOfUser)
                    .balance(balance)
                    .status(goal.getStatus())
                    .build();

            if (balance.compareTo(goal.getAmount()) >= 0) {
                goalSingleResponse.setPercentage(100);
            }else if(balance.compareTo(BigDecimal.ZERO) <= 0){
                goalSingleResponse.setPercentage(0);
            }else {
                goalSingleResponse.setPercentage(Math.round(balance.doubleValue() / goal.getAmount().doubleValue() * 100.0));
            }

            goalSingleResponses.add(goalSingleResponse);
        });
        return GoalResponse.builder()
                .goals(goalSingleResponses)
                .userId(user.getId())
                .build();
    }

    @Override
    public Boolean createGoal(GoalRequest goalRequest) {
        User user = getUser();
        Goal goal = Goal.builder()
                .user(user)
                .startDate(goalRequest.getGoalStateDate())
                .endDate(goalRequest.getGoalEndDate())
                .name(goalRequest.getGoalName())
                .amount(goalRequest.getAmount())
                .status(GoalStatus.PENDING)
                .build();
        if (goalRequest.getGoalStateDate().isBefore(LocalDate.now()) &&
                goalRequest.getGoalEndDate().isAfter(LocalDate.now())
        ) {
            goal.setStatus(GoalStatus.ACTIVE);
        } else if (goalRequest.getGoalStateDate().isBefore(LocalDate.now()) &&
                goalRequest.getGoalEndDate().isBefore(LocalDate.now())) {
            goal.setStatus(GoalStatus.COMPLETED);
        }

        goalRepository.save(goal);
        return true;
    }

    @Override
    public Boolean deleteGoal(Long id) {
        try {
            goalRepository.deleteByIdAndUser(id, getUser());
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
                        startOfMonth, endOfMonth).stream().map(Transaction::getTotalAmount)
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
