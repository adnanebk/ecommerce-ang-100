package com.adnanbk.ecommerceang.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class ContollerAdvice {


    @ExceptionHandler({ PersistenceException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolation(
            PersistenceException ex) {
        Set<String> errors = new HashSet<>();
        if(ex.getCause() instanceof ConstraintViolationException)
        {
            ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();

            for (ConstraintViolation<?> violation : cause.getConstraintViolations()) {
                String message = violation.getMessage();
                if(violation.getPropertyPath()!=null)
                    message = violation.getPropertyPath() + " "+violation.getMessage();
                errors.add(message);
            }
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolation0(
            ConstraintViolationException ex) {
        Set<String> errors = new HashSet<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String message = violation.getMessage();
            if(violation.getPropertyPath()!=null)
                message = violation.getPropertyPath() + " "+violation.getMessage();
            errors.add(message);
        }
        return ResponseEntity.badRequest().body(errors);
    }



}
