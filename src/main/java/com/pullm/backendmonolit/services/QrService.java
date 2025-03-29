package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.response.Receipt;

public interface QrService {

    Receipt getReceiptResponse(String fiscalId);
}
