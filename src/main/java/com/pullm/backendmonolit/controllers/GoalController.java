package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.enums.GoalStatus;
import com.pullm.backendmonolit.models.request.ChangeGoalStatusRequest;
import com.pullm.backendmonolit.models.request.GoalRequest;
import com.pullm.backendmonolit.models.response.GoalResponse;
import com.pullm.backendmonolit.models.response.GoalSingleResponse;
import com.pullm.backendmonolit.models.response.ResponseDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseDTO<GoalSingleResponse> getGoalById(Long id) {
        return ResponseDTO.<GoalSingleResponse>builder()
                .data(goalService.getGoal(id)).build();
    }

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<GoalResponse> getGoalByStatus(@RequestParam(value = "status", required = false) GoalStatus status,
                                                     @RequestParam(value = "count", required = false) Integer count) {
        return ResponseDTO.<GoalResponse>builder()
                .data(goalService.getAllGoals(status, count)).build();
    }

    @GetMapping("/history")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<GoalResponse> getGoalHistory() {
        return ResponseDTO.<GoalResponse>builder()
                .data(goalService.getGoalHistory()).build();
    }


    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<GoalSingleResponse> getGoal(@PathVariable("id") Long id) {
        return ResponseDTO.<GoalSingleResponse>builder()
                .data(goalService.getGoal(id)).build();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<String> createGoal(@RequestBody GoalRequest goalRequest) {
        return ResponseDTO.<String>builder()
                .data(goalService.createGoal(goalRequest)).build();
    }

    @PutMapping("update")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<Boolean> changeGoalStatus(@RequestBody ChangeGoalStatusRequest changeGoalStatusRequest) {
        return ResponseDTO.<Boolean>builder()
                .data(goalService.changeGoal(changeGoalStatusRequest)).build();
    }


    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<Boolean> deleteGoal(@PathVariable("id") Long id) {
        return ResponseDTO.<Boolean>builder()
                .data(goalService.deleteGoal(id)).build();
    }


}
