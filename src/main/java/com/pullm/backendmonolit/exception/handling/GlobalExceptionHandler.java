package com.pullm.backendmonolit.exception.handling;

import static com.pullm.backendmonolit.exception.handling.ExceptionsMessages.DUPLICATE_RESOURCE_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionsMessages.INVALID_OTP_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionsMessages.METHOD_ARGUMENT_NOT_VALID_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionsMessages.RESOURCE_NOT_FOUND_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionsMessages.UNEXPECTED_EXCEPTION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.pullm.backendmonolit.exception.DuplicateResourceException;
import com.pullm.backendmonolit.exception.InvalidOTPException;
import com.pullm.backendmonolit.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static List<String> getErrors(MethodArgumentNotValidException ex) {
    List<String> errors = new ArrayList<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.add(fieldName + ": " + errorMessage);
    });

    return errors;
  }

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public ErrorResponse handleException(ResourceNotFoundException e) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .customMessage(RESOURCE_NOT_FOUND_EXCEPTION.getMessage())
        .details(List.of(e.getLocalizedMessage()))
        .build();

    log.error("ResourceNotFoundException: ", e);

    return errorResponse;
  }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(BadCredentialsException.class)
  public ErrorResponse handleException(BadCredentialsException e) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .customMessage(RESOURCE_NOT_FOUND_EXCEPTION.getMessage())
        .details(List.of(e.getLocalizedMessage()))
        .build();

    log.error("BadCredentialsException: ", e);
    return errorResponse;
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
      HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .customMessage(UNEXPECTED_EXCEPTION.getMessage())
        .details(List.of(ex.getMessage()))
        .build();

    log.error("Exception: ", ex);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    ErrorResponse error = ErrorResponse.builder()
        .customMessage(METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getMessage())
        .details(getErrors(ex))
        .build();

    log.error("MethodArgumentNotValidException: ", ex);

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(InvalidOTPException.class)
  public ErrorResponse handleException(InvalidOTPException e) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .customMessage(INVALID_OTP_EXCEPTION.getMessage())
        .details(List.of(e.getLocalizedMessage()))
        .build();

    log.error("InvalidOTPException: ", e);
    return errorResponse;
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(DuplicateResourceException.class)
  public ErrorResponse handleException(DuplicateResourceException e) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .customMessage(DUPLICATE_RESOURCE_EXCEPTION.getMessage())
        .details(List.of(e.getLocalizedMessage()))
        .build();

    log.error("DuplicateResourceException: ", e);
    return errorResponse;
  }
}
