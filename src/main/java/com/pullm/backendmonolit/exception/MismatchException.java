package com.pullm.backendmonolit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class MismatchException extends RuntimeException {

    public MismatchException(String message) {
        super(message);
    }
}
