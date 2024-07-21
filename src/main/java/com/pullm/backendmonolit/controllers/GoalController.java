package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.enums.GoalStatus;
import com.pullm.backendmonolit.models.request.ChangeGoalStatusRequest;
import com.pullm.backendmonolit.models.request.GoalRequest;
import com.pullm.backendmonolit.models.response.GoalResponse;
import com.pullm.backendmonolit.models.response.GoalSingleResponse;
import com.pullm.backendmonolit.services.GoalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/goal")
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public GoalSingleResponse getGoalById(Long id) {
        return goalService.getGoal(id);
    }

    @GetMapping("/all/{status}")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public GoalResponse getGoalByStatus(@PathVariable("status") GoalStatus status) {
        return goalService.getAllGoals(status);
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public GoalSingleResponse getGoal(@PathVariable("id") Long id) {
        return goalService.getGoal(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public Boolean createGoal(@RequestBody GoalRequest goalRequest) {
        return goalService.createGoal(goalRequest);
    }

    @PutMapping("status-update")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public Boolean changeGoalStatus(@RequestBody ChangeGoalStatusRequest changeGoalStatusRequest) {
        return goalService.changeGoalStatus(changeGoalStatusRequest);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public Boolean deleteGoal(@PathVariable("id") Long id) {
        return goalService.deleteGoal(id);
    }


}
