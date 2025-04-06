package com.pullm.backendmonolit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.Decoder;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            String body = new String(response.body().asInputStream().readAllBytes(),
                    Charset.forName("ISO-8859-9"));

            return objectMapper.readValue(body, objectMapper.constructType(type));
        }
    }
}
