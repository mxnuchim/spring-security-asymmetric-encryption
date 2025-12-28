package com.mxnuchim.auth.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND("USER_NOT_FOUND", "Failed to fetch user", HttpStatus.NOT_FOUND),
    CHANGE_PASSWORDS_MISMATCH("CHANGE_PASSWORD_MISMATCH", "Password and confirm password are not the same" , HttpStatus.BAD_REQUEST),
    INVALID_CURRENT_PASSWORD("INVALID_CURRENT_PASSWORD","Current password is incorrect" , HttpStatus.BAD_REQUEST );

    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

    ErrorCode(
            final String code,
            final String defaultMessage,
            final HttpStatus status
    ){
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;

    }
}
