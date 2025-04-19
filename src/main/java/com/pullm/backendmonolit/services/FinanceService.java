package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.enums.FinanceRange;
import com.pullm.backendmonolit.models.request.AddIncomeRequest;
import com.pullm.backendmonolit.models.response.FinancialStatusOverallResponse;
import com.pullm.backendmonolit.models.response.FinancialStatusResponse;
import java.time.LocalDateTime;

public interface FinanceService {

    FinancialStatusResponse getFinancialCondition();

    Boolean addIncome(AddIncomeRequest addIncomeRequest);

    Boolean deleteIncome(Long id);

    FinancialStatusOverallResponse getFinancialCondition(FinanceRange financeRange, LocalDateTime startDate,
                                                         LocalDateTime endDate);

}
