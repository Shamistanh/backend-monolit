package com.pullm.backendmonolit.exception.handling;

import com.pullm.backendmonolit.exception.DuplicateResourceException;
import com.pullm.backendmonolit.exception.MismatchException;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.exception.OtpException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pullm.backendmonolit.exception.handling.ExceptionMessage.BAD_CREDENTIALS_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionMessage.DUPLICATE_RESOURCE_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionMessage.INVALID_OTP_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionMessage.METHOD_ARGUMENT_NOT_VALID_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionMessage.MISMATCH_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionMessage.NOT_FOUND_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionMessage.UNEXPECTED_EXCEPTION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception ex) {
        log.error("Exception: ", ex);
        return getErrorResponse(ex, UNEXPECTED_EXCEPTION);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleException(NotFoundException e) {
        log.error("ResourceNotFoundException: ", e);
        return getErrorResponse(e, NOT_FOUND_EXCEPTION);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handleException(BadCredentialsException e) {
        log.error("BadCredentialsException: ", e);
        return getErrorResponse(e, BAD_CREDENTIALS_EXCEPTION);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: ", ex);

        return ErrorResponse.builder()
                .error(METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getMessage())
                .messages(getErrors(ex))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(OtpException.class)
    public ErrorResponse handleException(OtpException e) {
        log.error("InvalidOTPException: ", e);
        return getErrorResponse(e, INVALID_OTP_EXCEPTION);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DuplicateResourceException.class)
    public ErrorResponse handleException(DuplicateResourceException e) {
        log.error("DuplicateResourceException: ", e);
        return getErrorResponse(e, DUPLICATE_RESOURCE_EXCEPTION);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MismatchException.class)
    public ErrorResponse handleException(MismatchException e) {
        log.error("MismatchException: ", e);
        return getErrorResponse(e, MISMATCH_EXCEPTION);
    }

  private static ErrorResponse getErrorResponse(Exception ex, ExceptionMessage message) {
    return ErrorResponse.builder()
            .messages(List.of(message.getMessage()))
            .timestamp(LocalDateTime.now())
            .error(ex.getMessage())
            .build();
  }

  private static List<String> getErrors(MethodArgumentNotValidException ex) {
    List<String> errors = new ArrayList<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.add("%s : %s".formatted(fieldName, errorMessage));
    });

    return errors;
  }

}
