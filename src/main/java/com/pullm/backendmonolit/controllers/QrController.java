package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.models.response.Receipt;
import com.pullm.backendmonolit.models.response.ResponseDTO;
import com.pullm.backendmonolit.services.QrService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/qr")
public class QrController {

    private final QrService qrService;

    @GetMapping("/receipt")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<Receipt> getQrData(@RequestParam String fiscalId) {
        return ResponseDTO.<Receipt>builder().data(qrService.getReceiptResponse(fiscalId)).build();
    }

}
