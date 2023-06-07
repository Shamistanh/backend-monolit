//package com.pullm.backendmonolit.exception.handling;
//
//import static com.pullm.backendmonolit.exception.handling.ExceptionsMessages.UNEXPECTED_EXCEPTION;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//
//@Component
//@RequiredArgsConstructor
//public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//  private final HandlerExceptionResolver handlerExceptionResolver;
//
//  @Override
//  public void commence(HttpServletRequest request, HttpServletResponse response,
//      AuthenticationException exception) {
//
//    ErrorResponse errorResponse = ErrorResponse.builder()
//        .customMessage(UNEXPECTED_EXCEPTION.getMessage())
//        .details(List.of(exception.getLocalizedMessage()))
//        .build();
//
//    handlerExceptionResolver.resolveException(request, response, errorResponse, exception);
//  }
//
//}
