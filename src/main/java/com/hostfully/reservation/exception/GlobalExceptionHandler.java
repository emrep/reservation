package com.hostfully.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(
            ItemNotFoundException exception,
            WebRequest request
    ){
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(OverlappingBookingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleOverlappingBookingException(
            OverlappingBookingException exception,
            WebRequest request
    ){
        return buildErrorResponse(exception, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception exception,
            WebRequest request){
        return buildErrorResponse(
                exception,
                "Unknown error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                httpStatus,
                request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                exception.getMessage()
        );

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
