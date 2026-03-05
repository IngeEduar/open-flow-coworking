package com.nelumbo.open_flow_coworking.exception.Handler;

import com.nelumbo.open_flow_coworking.exception.ApiExceptionError.ApiExceptionError;
import com.nelumbo.open_flow_coworking.exception.OpenFlowException;
import com.nelumbo.open_flow_coworking.model.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult().getFieldError();

        if (fieldError == null) {
            ErrorResponse error = new ErrorResponse(
                    "Invalid request",
                    HttpStatus.BAD_REQUEST,
                    7
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        OpenFlowException exception = new OpenFlowException(
                7,
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );

        ErrorResponse response = new ErrorResponse(
                exception.getMessage(),
                exception.getHttpStatus(),
                exception.getCode()
        );

        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

    @ExceptionHandler(OpenFlowException.class)
    public ResponseEntity<ErrorResponse> handleCustom(OpenFlowException ex) {

        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                ex.getHttpStatus(),
                ex.getCode()
        );

        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handle404(NoHandlerFoundException ex) {

        int code = 6;
        String message = ApiExceptionError.formatMessage(code, ex.getRequestURL());

        ErrorResponse response = new ErrorResponse(
                message,
                HttpStatus.NOT_FOUND,
                code
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleGeneral(AuthorizationDeniedException ex) {

        int code = 1005;
        String message = ApiExceptionError.formatMessage(code);

        ErrorResponse response = new ErrorResponse(
                message,
                HttpStatus.FORBIDDEN,
                code
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("[ERROR] {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                ApiExceptionError.formatMessage(1),
                HttpStatus.INTERNAL_SERVER_ERROR,
                1
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
