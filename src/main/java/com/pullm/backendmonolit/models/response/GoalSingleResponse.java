package com.pullm.backendmonolit.models.response;

import com.pullm.backendmonolit.enums.GoalPriority;
import com.pullm.backendmonolit.enums.GoalStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GoalSingleResponse {
    private GoalStatus status;
    private String name;
    private BigDecimal amount;
    private BigDecimal balance;
    private BigDecimal monthlyTotalExpenses;
    private GoalPriority priority;
    private LocalDate endDate;
    private LocalDate startDate;
    private long percentage;


}
