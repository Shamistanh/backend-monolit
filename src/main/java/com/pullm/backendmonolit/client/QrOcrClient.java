package com.pullm.backendmonolit.client;

import com.pullm.backendmonolit.models.response.Receipt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "receiptClient", url = "https://vvaly-qr-scan-a8f90cbd9b9e.herokuapp.com")
public interface QrOcrClient {

    @PostMapping(value = "/upload-receipt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Receipt uploadReceipt(@RequestPart("file") MultipartFile file);

}