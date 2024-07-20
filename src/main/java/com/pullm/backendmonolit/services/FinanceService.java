package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.request.AddIncomeRequest;
import com.pullm.backendmonolit.models.response.FinancialStatusResponse;

public interface FinanceService {

    FinancialStatusResponse getFinancialCondition();

    Boolean addIncome(AddIncomeRequest addIncomeRequest);

    Boolean deleteIncome(Long id);

}
