package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.enums.FinanceRange;
import com.pullm.backendmonolit.models.request.AddIncomeRequest;
import com.pullm.backendmonolit.models.response.FinancialStatusOverallResponse;
import com.pullm.backendmonolit.models.response.FinancialStatusResponse;
import com.pullm.backendmonolit.models.response.ResponseDTO;
import com.pullm.backendmonolit.services.FinanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/finance")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<FinancialStatusResponse> getFinanceOverview() {
        return ResponseDTO.<FinancialStatusResponse>builder()
                .data(financeService.getFinancialCondition()).build();
    }

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<FinancialStatusOverallResponse> getOverallFinanceOverview(
            @RequestParam FinanceRange financeRange,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    ) {
        return ResponseDTO.<FinancialStatusOverallResponse>builder()
                .data(financeService.getFinancialCondition(financeRange, startDate, endDate)).build();
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<Boolean> addIncome(@RequestBody AddIncomeRequest addIncomeRequest) {
        return ResponseDTO.<Boolean>builder()
                .data(financeService.addIncome(addIncomeRequest)).build();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<Boolean> deleteGoal(@PathVariable("id") Long id) {
        return ResponseDTO.<Boolean>builder()
                .data(financeService.deleteIncome(id)).build();
    }


}
