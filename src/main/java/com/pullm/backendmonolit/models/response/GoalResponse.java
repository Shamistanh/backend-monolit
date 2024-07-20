package com.pullm.backendmonolit.models.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GoalResponse {
    Long userId;
    List<GoalSingleResponse> goals;

}
