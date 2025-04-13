package com.pullm.backendmonolit.models.request;

import com.pullm.backendmonolit.enums.GoalPriority;
import com.pullm.backendmonolit.enums.GoalStatus;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class ChangeGoalStatusRequest {
    private Long goalId;
    private GoalStatus status;
    private Date endDate;
    private BigDecimal amount;
    private GoalPriority priority;

}
