package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.response.ProcessedReceipt;
import com.pullm.backendmonolit.models.response.Receipt;

public interface QrService {

    Receipt getReceiptResponse(String fiscalId);

    ProcessedReceipt processReceiptResponse(String imageUrl);

}
