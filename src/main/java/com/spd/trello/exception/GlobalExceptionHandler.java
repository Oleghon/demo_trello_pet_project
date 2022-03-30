package com.spd.trello.exception;

import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handle(RuntimeException ex, WebRequest request) {
        String responseBody = "Something went wrong...";
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(),
                HttpStatus.I_AM_A_TEAPOT, request);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(ConstraintViolationException ex) {
        List<String> details = new ArrayList<>();

        ex.getConstraintViolations().forEach(v -> details.add(v.getMessage()));
        ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}

@Data
class ErrorResponse {
    public ErrorResponse(String message, List<String> details) {
        this.message = message;
        this.details = details;
    }

    private String message;

    private List<String> details;

}