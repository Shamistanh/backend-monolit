package com.pullm.backendmonolit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TokenNotValidException extends RuntimeException{
    public TokenNotValidException(String message) {
        super(message);
    }
}
