package com.pullm.backendmonolit.exception.handling;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    log.error("Unauthorized error: {}", authException.getMessage());

    ErrorResponse errorResponseDetail = ErrorResponse.builder()
        .messages(List.of(ExceptionMessage.UNAUTHORIZED.getMessage()))
        .error(authException.getMessage())
        .timestamp(LocalDateTime.now())
        .build();

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(SC_UNAUTHORIZED);

    OutputStream responseStream = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.writeValue(responseStream, errorResponseDetail);

    responseStream.flush();
  }

}
