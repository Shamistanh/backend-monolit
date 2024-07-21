package com.pullm.backendmonolit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullm.backendmonolit.services.ConversionService;
import com.pullm.backendmonolit.services.impl.ConversionServiceImpl;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@WebFilter("/*")
@Configuration
@RequiredArgsConstructor
public class ResponseCaptureFilter implements Filter {

    private final ConversionService conversionService;
    private final ObjectMapper objectMapper;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if necessary
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            CustomHttpServletResponseWrapper responseWrapper = new CustomHttpServletResponseWrapper((HttpServletResponse) response);
            chain.doFilter(request, responseWrapper);

            String responseContent = responseWrapper.getResponseAsString();
            String modifiedResponseBody = responseContent;


            try {
                Object responseBody = objectMapper.readValue(responseContent, Object.class);

                if (responseBody instanceof Map) {
                    Map<String, Object> responseBodyMap = (Map<String, Object>) responseBody;
                    processAmounts(responseBodyMap);
                    modifiedResponseBody = objectMapper.writeValueAsString(responseBodyMap);
                } else if (responseBody instanceof List) {
                    List<Object> responseBodyList = (List<Object>) responseBody;
                    processAmounts(responseBodyList);
                    modifiedResponseBody = objectMapper.writeValueAsString(responseBodyList);
                } else {
                    // Handle unexpected response type
                    System.out.println("Unexpected response type: " + responseBody.getClass().getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;

            }



            response.getWriter().write(modifiedResponseBody);

        } else {
            chain.doFilter(request, response);
        }
    }

    private void processAmounts(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                processAmounts((Map<String, Object>) entry.getValue());
            } else if (entry.getValue() instanceof Iterable) {
                for (Object item : (Iterable) entry.getValue()) {
                    if (item instanceof Map) {
                        processAmounts((Map<String, Object>) item);
                    }
                }
            } else if ("amount".equals(entry.getKey()) && entry.getValue() instanceof Number
            || "balance".equals(entry.getKey()) && entry.getValue() instanceof Number
            || "monthlyTotalExpenses".equals(entry.getKey()) && entry.getValue() instanceof Number
            || "monthlyIncome".equals(entry.getKey()) && entry.getValue() instanceof Number) {
                double originalAmount = ((Number) entry.getValue()).doubleValue();
                double convertedAmount = conversionService.convertAmount(originalAmount);
                entry.setValue(convertedAmount);
            }
        }
    }

    public void processAmounts(List<Object> list) {
        for (Object item : list) {
            if (item instanceof Map) {
                processAmounts((Map<String, Object>) item);
            } else if (item instanceof List) {
                processAmounts((List<Object>) item);
            }
        }
    }

    @Override
    public void destroy() {
        // Cleanup code if necessary
    }
}
