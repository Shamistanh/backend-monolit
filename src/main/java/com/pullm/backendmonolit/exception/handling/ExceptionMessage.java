package com.pullm.backendmonolit.exception.handling;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {

    UNEXPECTED_EXCEPTION("Unexpected exception occurred"),
    NOT_FOUND_EXCEPTION("Resource not found exception occurred"),
    INSUFFICIENT_AUTHENTICATION_EXCEPTION("Insufficient authentication exception occurred"),
    BAD_CREDENTIALS_EXCEPTION("Bad credentials exception occurred"),
    DUPLICATE_RESOURCE_EXCEPTION("Username already exists exception occurred"),
    AUTHENTICATION_FAILED("Authentication failed exception occurred"),
    UNAUTHORIZED("Unauthorized exception occurred"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("Method argument not valid exception occurred"),
    INVALID_OTP_EXCEPTION("Invalid OTP exception occurred"),
    MISMATCH_EXCEPTION("Passwords are not the same"),
    ;

    private final String message;
}
