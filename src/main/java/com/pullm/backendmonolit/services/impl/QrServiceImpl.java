package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.client.QrAiProcessorClient;
import com.pullm.backendmonolit.client.QrOcrClient;
import com.pullm.backendmonolit.models.request.ProcessingReceipt;
import com.pullm.backendmonolit.models.response.ByteArrayMultipartFile;
import com.pullm.backendmonolit.models.response.ProcessedReceipt;
import com.pullm.backendmonolit.models.response.Receipt;
import com.pullm.backendmonolit.services.QrService;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import liquibase.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class QrServiceImpl implements QrService {

    private final QrOcrClient qrOcrClient;

    private final QrAiProcessorClient qrAiProcessorClient;

    @Value("${ekassa.url}")
    private String ekassaUrl;

    @Value("${vat.cashback-rates.cash}")
    private BigDecimal cashRate;

    @Value("${vat.cashback-rates.cashless}")
    private BigDecimal cashlessRate;

    @Override
    public Receipt getReceiptResponse(String fiscalId) {
        String imageUrl = ekassaUrl + fiscalId;
        try {
            return qrOcrClient.uploadReceipt(downloadImage(imageUrl));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public ProcessedReceipt processReceiptResponse(String fiscalId) {
        if (StringUtil.isEmpty(fiscalId)) {
            throw new IllegalArgumentException("fiscalId cannot be empty");
        }
        String imageUrl = ekassaUrl + fiscalId;
        try {
            ProcessedReceipt processedReceipt = qrAiProcessorClient.processReceipt(new ProcessingReceipt(imageUrl));
            addVatCashbackAmount(processedReceipt);
            return processedReceipt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addVatCashbackAmount(ProcessedReceipt processedReceipt) {
        ProcessedReceipt.PaymentType paymentType = processedReceipt.getPaymentType();
        if (BigDecimal.ZERO.compareTo(paymentType.getCash()) == 0
            && BigDecimal.ZERO.compareTo(paymentType.getCashless()) != 0) {
            processedReceipt.setVatCashbackAmount(paymentType.getCashless().multiply(cashlessRate)
                    .divide(BigDecimal.valueOf(100),
                    RoundingMode.HALF_UP));
        } else if (BigDecimal.ZERO.compareTo(paymentType.getCashless()) == 0
                && BigDecimal.ZERO.compareTo(paymentType.getCash()) != 0) {
            processedReceipt.setVatCashbackAmount(paymentType.getCash().multiply(cashRate).divide(BigDecimal.valueOf(100),
                    RoundingMode.HALF_UP));
        }
    }


    public MultipartFile downloadImage(String imageUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .GET()
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() == 200) {
            byte[] imageBytes = StreamUtils.copyToByteArray(response.body());
            return new ByteArrayMultipartFile(imageBytes, "image.jpg", "image/jpeg");
        } else {
            throw new IOException("Failed to download image, HTTP status: " + response.statusCode());
        }
    }

}
