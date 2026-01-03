package com.mxnuchim.auth.handler;

import com.mxnuchim.auth.exceptions.BusinessException;
import com.mxnuchim.auth.exceptions.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.mxnuchim.auth.exceptions.ErrorCode.INTERNAL_EXCEPTION;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleException (final BusinessException ex){
        final ErrorResponse body = ErrorResponse.builder()
                .code(ex.getErrorCode()
                        .getCode())
                .message(ex.getMessage())
                .build();
        log.info("Business Exception: {}", body);
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(ex.getErrorCode()
                        .getStatus() != null ? ex.getErrorCode()
                        .getStatus() : HttpStatus.BAD_REQUEST)
                .body(body);

    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleException() {
        final ErrorResponse body = ErrorResponse.builder()
                .code(ErrorCode.ERR_USER_DISABLED.getCode())
                .message(ErrorCode.ERR_USER_DISABLED.getDefaultMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(final BadCredentialsException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                .message(ErrorCode.BAD_CREDENTIALS.getDefaultMessage())
                .code(ErrorCode.BAD_CREDENTIALS.getCode())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final UsernameNotFoundException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.USERNAME_NOT_FOUND.getCode())
                .message(ErrorCode.USERNAME_NOT_FOUND.getDefaultMessage())
                .build();
        return new ResponseEntity<>(response,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException exp) {
        final List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        exp.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    final String fieldName = ((FieldError) error).getField();
                    final String errorCode = error.getDefaultMessage();
                    errors.add(ErrorResponse.ValidationError.builder()
                            .field(fieldName)
                            .code(errorCode)
                            .message(errorCode)
                            .build());
                });
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .validationErrors(errors)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final EntityNotFoundException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("ENTITY_NOT_FOUND")
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                .code(INTERNAL_EXCEPTION.getCode())
                .message(INTERNAL_EXCEPTION.getDefaultMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
