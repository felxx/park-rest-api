package com.felxx.park_rest_api.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.felxx.park_rest_api.exceptions.UsernameUniqueViolationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UsernameUniqueViolationException.class)
    public ResponseEntity<ErrorMessage> UsernameUniqueViolationException(UsernameUniqueViolationException exception, HttpServletRequest request, BindingResult bindingResult) {
        log.error("Api Error - ", exception);
        ErrorMessage errorMessage = new ErrorMessage(request, HttpStatus.CONFLICT, exception.getMessage(), bindingResult);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);

    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request, BindingResult bindingResult) {
        log.error("Api Error - ", exception);
        ErrorMessage errorMessage = new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Invalid field(s)", bindingResult);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessage);

    }
}
