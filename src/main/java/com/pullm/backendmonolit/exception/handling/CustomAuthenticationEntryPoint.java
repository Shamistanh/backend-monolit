package com.pullm.backendmonolit.exception.handling;

import static com.pullm.backendmonolit.exception.handling.ExceptionMessage.AUTHENTICATION_FAILED;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
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
        .message(ExceptionMessage.UNAUTHORIZED.getMessage())
        .error(authException.getMessage())
        .timestamp(LocalDateTime.now())
        .build();

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(SC_UNAUTHORIZED);

    OutputStream responseStream = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(responseStream, errorResponseDetail);

//    responseStream.flush();
  }

}
