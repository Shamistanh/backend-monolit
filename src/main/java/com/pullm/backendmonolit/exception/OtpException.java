package com.pullm.backendmonolit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class OtpException extends RuntimeException {
    public OtpException(String message) {
        super(message);
    }
}
