package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.enums.GoalStatus;
import com.pullm.backendmonolit.models.request.GoalRequest;
import com.pullm.backendmonolit.models.response.GoalResponse;
import com.pullm.backendmonolit.models.response.GoalSingleResponse;

public interface GoalService {

    GoalSingleResponse getGoal(Long id);

    GoalResponse getAllGoals(GoalStatus status);

    Boolean createGoal(GoalRequest goalRequest);

    Boolean deleteGoal(Long id);

}
