package com.pullm.backendmonolit.client;

import com.pullm.backendmonolit.config.FeignClientConfig;
import com.pullm.backendmonolit.models.request.ProcessingReceipt;
import com.pullm.backendmonolit.models.response.ProcessedReceipt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "qrAiClient", url = "https://vvaly-qr-scan-2-eeeedb8515da.herokuapp.com",
        configuration = FeignClientConfig.class)
public interface QrAiProcessorClient {

    @PostMapping(value = "/process_receipt")
    ProcessedReceipt processReceipt(@RequestBody ProcessingReceipt receipt);

}