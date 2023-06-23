package com.pullm.backendmonolit.exception.handling;

import static com.pullm.backendmonolit.exception.handling.ExceptionsMessages.AUTHENTICATION_FAILED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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

    ErrorResponse errorResponse = ErrorResponse.builder()
        .customMessage(AUTHENTICATION_FAILED.getMessage())
        .details(List.of(authException.getLocalizedMessage()))
        .build();

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(UNAUTHORIZED.value());

    OutputStream responseStream = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(responseStream, errorResponse);

    responseStream.flush();
  }

}
