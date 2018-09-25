package com.n26.web.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.n26.exception.InvalidJsonException;
import com.n26.exception.NotParseableFieldsException;
import com.n26.exception.TransactionOlderException;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(TransactionOlderException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public ErrorResponse processConcurrencyError(TransactionOlderException ex) {
        return new ErrorResponse(HttpStatus.NO_CONTENT.value(), ex.getMessage());
    }
    
    @ExceptionHandler(InvalidJsonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse processConcurrencyError(InvalidJsonException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
    
    @ExceptionHandler(NotParseableFieldsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse processConcurrencyError(NotParseableFieldsException ex) {
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse processValidationError(MethodArgumentNotValidException ex) {
        ErrorResponse dto = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return dto;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse processMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

}
