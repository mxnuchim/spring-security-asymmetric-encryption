package com.mxnuchim.auth.exceptions;

import jdk.jshell.Diag;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND("USER_NOT_FOUND", "Failed to fetch user", HttpStatus.NOT_FOUND),
    CHANGE_PASSWORDS_MISMATCH("CHANGE_PASSWORD_MISMATCH", "Password and confirm password are not the same" , HttpStatus.BAD_REQUEST),
    INVALID_CURRENT_PASSWORD("INVALID_CURRENT_PASSWORD","Current password is incorrect" , HttpStatus.BAD_REQUEST ),
    ACCOUNT_ALREADY_DEACTIVATED("ACCOUNT_ALREADY_DEACTIVATED","Account is already deactivated", HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_ACTIVATED("ACCOUNT_ALREADY_ACTIVATED","Account is already activated" ,  HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS","This email is already in use" , HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS("PHONE_ALREADY_EXISTS","This phone number is already in use" ,  HttpStatus.BAD_REQUEST),
    MISSING_PASSWORDS("MISSING_PASSWORDS","Password and Confirm Password are Required" ,  HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH("PASSWORD_MISMATCH","Passwords do not match" , HttpStatus.BAD_REQUEST ),
    ERR_USER_DISABLED("ERR_USER_DISABLED", "This account is disabled. Please contact support", HttpStatus.UNAUTHORIZED),
    BAD_CREDENTIALS("BAD_CREDENTIALS", "Username or password is incorrect", HttpStatus.UNAUTHORIZED),
    USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND),
    INTERNAL_EXCEPTION("INTERNAL_EXCEPTION", "", HttpStatus.INTERNAL_SERVER_ERROR);

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
