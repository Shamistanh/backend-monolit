package com.pullm.backendmonolit.models.request;

import com.pullm.backendmonolit.enums.GoalStatus;
import lombok.Data;

@Data
public class ChangeGoalStatusRequest {
    private Long goalId;
    private GoalStatus status;

}
