package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.Goal;
import com.pullm.backendmonolit.entities.Transaction;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.entities.UserIncome;
import com.pullm.backendmonolit.enums.GoalStatus;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.request.ChangeGoalStatusRequest;
import com.pullm.backendmonolit.models.request.GoalRequest;
import com.pullm.backendmonolit.models.response.GoalResponse;
import com.pullm.backendmonolit.models.response.GoalSingleResponse;
import com.pullm.backendmonolit.repository.GoalRepository;
import com.pullm.backendmonolit.repository.TransactionRepository;
import com.pullm.backendmonolit.repository.UserIncomeRepository;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.GoalService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
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

    private final UserIncomeRepository userIncomeRepository;

    @Override
    public GoalSingleResponse getGoal(Long id) {
        User user = getUser();
        Goal goal = goalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NoSuchElementException("Goal not found for user with id: " + id));

        BigDecimal monthlyExpense = findMonthlyExpenseOfUser();
        BigDecimal monthlyIncome = findMonthlyIncomeOfUser();
        BigDecimal balance = monthlyIncome.subtract(monthlyExpense);

        int percentage = 0;
        if (goal.getStatus() == GoalStatus.ACTIVE) {
            if (balance.compareTo(goal.getAmount()) >= 0) {
                percentage = 100;
            } else if (balance.compareTo(BigDecimal.ZERO) <= 0) {
                percentage = 0;
            } else {
                percentage = Math.round(balance
                        .divide(goal.getAmount(), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .floatValue());
            }
        }

        return GoalSingleResponse.builder()
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .name(goal.getName())
                .amount(goal.getAmount())
                .monthlyTotalExpenses(monthlyExpense)
                .balance(balance)
                .priority(goal.getGoalPriority())
                .status(goal.getStatus())
                .percentage(percentage)
                .build();
    }

    @Override
    public GoalResponse getAllGoals(GoalStatus status, Integer count) {
        User user = getUser();
        List<Goal> goals;
        if (count == null) {
            goals = (status != null)
                    ? goalRepository.findAllByUserAndStatus(user, status)
                    : goalRepository.findAllByUser(user);
        }else {
            goals = (status != null)
                    ? goalRepository.findAllByUserAndStatus(user, status, PageRequest.of(0, count))
                    : goalRepository.findAllByUser(user, PageRequest.of(0, count));
        }


        BigDecimal monthlyExpense = findMonthlyExpenseOfUser();
        BigDecimal monthlyIncome = findMonthlyIncomeOfUser();
        BigDecimal balance = monthlyIncome.subtract(monthlyExpense);

        List<GoalSingleResponse> responses = new ArrayList<>();
        BigDecimal remainingBalance = balance;

        for (Goal goal : goals) {
            GoalSingleResponse.GoalSingleResponseBuilder builder = GoalSingleResponse.builder()
                    .id(goal.getId())
                    .startDate(goal.getStartDate())
                    .endDate(goal.getEndDate())
                    .name(goal.getName())
                    .amount(goal.getAmount())
                    .monthlyTotalExpenses(monthlyExpense)
                    .balance(balance)
                    .priority(goal.getGoalPriority())
                    .status(goal.getStatus());

            if (goal.getStatus() == GoalStatus.ACTIVE) {
                int percentage = 0;

                if (remainingBalance.compareTo(BigDecimal.ZERO) > 0) {
                    if (remainingBalance.compareTo(goal.getAmount()) >= 0) {
                        percentage = 100;
                        remainingBalance = remainingBalance.subtract(goal.getAmount());
                    } else {
                        percentage = Math.round(remainingBalance
                                .divide(goal.getAmount(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100))
                                .floatValue());
                    }
                }

                builder.percentage(percentage);
            }

            responses.add(builder.build());
        }

        return GoalResponse.builder()
                .userId(user.getId())
                .goals(responses)
                .build();
    }


    @Override
    public String createGoal(GoalRequest goalRequest) {

        GoalStatus requestedStatus = goalRequest.getStatus();

        if (requestedStatus == GoalStatus.COMPLETED) {
           throw new RuntimeException("Goal with COMPLETED status can't be created");
        }

        User user = getUser();

        boolean alreadyHasActiveGoal = goalRepository.existsByUserAndGoalPriorityAndStatus(
                user, goalRequest.getGoalPriority(), GoalStatus.ACTIVE);

        GoalStatus finalStatus;

        if (requestedStatus == GoalStatus.ACTIVE && alreadyHasActiveGoal) {
            finalStatus = GoalStatus.PENDING;
        } else {
            finalStatus = requestedStatus;
        }

        Goal newGoal = Goal.builder()
                .user(user)
                .name(goalRequest.getGoalName())
                .amount(goalRequest.getAmount())
                .startDate(LocalDate.now())
                .endDate(goalRequest.getGoalEndDate())
                .goalPriority(goalRequest.getGoalPriority())
                .status(finalStatus)
                .build();

        goalRepository.save(newGoal);

        if (requestedStatus == GoalStatus.PENDING) {
            return "Your goal has been set to PENDING as requested.";
        }

        return finalStatus == GoalStatus.PENDING
                ? "You already have an active goal with this priority, so the new goal is set to PENDING."
                : "Your goal has been set to ACTIVE.";
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

    @Override
    public Boolean changeGoal(ChangeGoalStatusRequest request) {
        return goalRepository.findByIdAndUser(request.getGoalId(), getUser())
                .filter(goal -> {
                    if (request.getEndDate() != null) {
                        LocalDate newEndDate = request.getEndDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        if (newEndDate.isBefore(LocalDate.now())) {
                            log.warn("Update rejected: endDate {} has already passed", newEndDate);
                            return false;
                        }
                    }
                    return true;
                })
                .map(goal -> {
                    boolean updated = false;

                    if (request.getStatus() != null) {
                        goal.setStatus(request.getStatus());
                        updated = true;
                    }

                    if (request.getEndDate() != null) {
                        LocalDate newEndDate = request.getEndDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        goal.setEndDate(newEndDate);
                        updated = true;
                    }

                    if (request.getPriority() != null) {
                        if (!goalRepository.existsByUserAndGoalPriorityAndStatus(getUser(), request.getPriority(),
                                GoalStatus.ACTIVE)) {
                            goal.setGoalPriority(request.getPriority());
                            updated = true;
                        } else {
                           log.info("Goal with priority {} already exists", request.getPriority());
                        }

                    }

                    if (request.getAmount() != null  && request.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                        goal.setAmount(request.getAmount());
                        updated = true;
                    }

                    if (updated) {
                        goalRepository.save(goal);
                    }

                    return updated;
                })
                .orElse(false);
    }

    @Override
    public GoalResponse getGoalHistory() {
        User user = getUser();
        List<Goal> allByUserAndEndDateBefore = goalRepository.findAllByUserAndEndDateBeforeOrStatus(user,
                LocalDate.now(), GoalStatus.COMPLETED);
        List<GoalSingleResponse> goalSingleResponseList = new ArrayList<>();
        allByUserAndEndDateBefore.forEach(goal -> {
            goalSingleResponseList.add( GoalSingleResponse.builder()
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .name(goal.getName())
                .amount(goal.getAmount())
                .priority(goal.getGoalPriority())
                .status(goal.getStatus())
                .build());
        });
        return GoalResponse.builder()
                .userId(user.getId())
                .goals(goalSingleResponseList)
                .build();
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
