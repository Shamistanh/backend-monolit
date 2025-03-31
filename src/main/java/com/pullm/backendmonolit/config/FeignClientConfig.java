package com.pullm.backendmonolit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.lang.reflect.Type;

@Configuration
public class FeignClientConfig {

    @Bean
    public Decoder customDecoder() {
        return new CustomFeignDecoder(new ObjectMapper());
    }

    public static class CustomFeignDecoder implements Decoder {
        private final ObjectMapper objectMapper;

        public CustomFeignDecoder(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Object decode(Response response, Type type) throws IOException {
            // Read the response body
            String body = new String(response.body().asInputStream().readAllBytes());
            
            // Try to parse JSON manually, even if Content-Type is text/html
            return objectMapper.readValue(body, objectMapper.constructType(type));
        }
    }
}
