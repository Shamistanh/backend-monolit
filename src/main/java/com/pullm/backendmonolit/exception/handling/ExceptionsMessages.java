package com.pullm.backendmonolit.exception.handling;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionsMessages {

  UNEXPECTED_EXCEPTION("Unexpected exception occurred"),
  RESOURCE_NOT_FOUND_EXCEPTION("Resource not found exception occurred"),
  INSUFFICIENT_AUTHENTICATION_EXCEPTION("Insufficient authentication exception occurred"),
  BAD_CREDENTIALS_EXCEPTION("Bad credentials occurred exception occurred"),
  DUPLICATE_RESOURCE_EXCEPTION("Username already exists exception occurred"),
  AUTHENTICATION_FAILED("Authentication failed exception occurred"),
  METHOD_ARGUMENT_NOT_VALID_EXCEPTION("Method argument not valid exception occurred"),
  INVALID_OTP_EXCEPTION("Invalid OTP exception occurred"),
  ;

  private final String message;
}
