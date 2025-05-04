package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.enums.GoalPriority;
import com.pullm.backendmonolit.enums.GoalStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GoalSingleResponse implements Comparable<GoalSingleResponse> {
    private Long id;
    private GoalStatus status;
    private String name;
    private BigDecimal amount;
    private BigDecimal balance;
    private BigDecimal monthlyTotalExpenses;
    private GoalPriority priority;
    private LocalDate endDate;
    private LocalDate startDate;
    private long percentage;

    @Override
    public int compareTo(GoalSingleResponse o) {
        int statusComparison = getStatusPriority(this.status) - getStatusPriority(o.status);
        if (statusComparison != 0) {
            return statusComparison;
        }
        return getPriorityValue(this.priority) - getPriorityValue(o.priority);
    }

    private int getStatusPriority(GoalStatus status) {
        return switch (status) {
            case ACTIVE -> 0;
            case PENDING -> 1;
            case COMPLETED -> 2;
        };
    }

    private int getPriorityValue(GoalPriority priority) {
        return switch (priority) {
            case HIGH -> 0;
            case MEDIUM -> 1;
            case LOW -> 2;
        };
    }

}
