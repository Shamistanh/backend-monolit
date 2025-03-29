package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.client.QrOcrClient;
import com.pullm.backendmonolit.models.response.ByteArrayMultipartFile;
import com.pullm.backendmonolit.models.response.Receipt;
import com.pullm.backendmonolit.services.QrService;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    @Value("${ekassa.url}")
    private String ekassaUrl;

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
