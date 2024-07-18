package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.request.FinancialStatusRequest;
import com.pullm.backendmonolit.models.response.FinancialStatusResponse;

public interface FinanceService {

    FinancialStatusResponse getFinancialCondition();

    Boolean changeAmount(FinancialStatusRequest financialStatusRequest);

}
