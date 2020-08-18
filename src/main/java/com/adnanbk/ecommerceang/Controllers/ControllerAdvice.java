package com.adnanbk.ecommerceang.Controllers;

import com.adnanbk.ecommerceang.dto.ApiError;
import com.adnanbk.ecommerceang.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {



    @ExceptionHandler({ ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolation0(
            ConstraintViolationException ex) {

        Set<String> errors = generateErrors(ex);

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({ PersistenceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolation(
            PersistenceException ex) {

        if(ex.getCause() instanceof ConstraintViolationException)
        {
            ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();

            Set<String> errors  = generateErrors(cause);
            return ResponseEntity.badRequest().body(errors);
        }

        if(ex.getCause() instanceof SQLIntegrityConstraintViolationException)
        {

            return ResponseEntity.badRequest().body("You are trying to insert an existing value  , try another one");

        }

        return ResponseEntity.badRequest().body("An error has been thrown during database modification ");
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        Set<ResponseError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> new ResponseError(x.getField(),x.getDefaultMessage()))
                .collect(Collectors.toSet());

        ex.getBindingResult().getGlobalErrors().stream()
                .forEach(x -> {
                    if(x.getCode().toLowerCase().contains("confirmpassword"))
                        errors.add(new ResponseError("confirmPassword",x.getDefaultMessage()));
                });

        // body.put("errors", errors);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Try to fix these errors", errors);
        return new  ResponseEntity(apiError,HttpStatus.BAD_REQUEST);
    }

    private Set<String> generateErrors(ConstraintViolationException cause) {
        Set<String> errors = new HashSet<>();
        for (ConstraintViolation<?> violation : cause.getConstraintViolations()) {
            String message = violation.getMessage();
            if(violation.getPropertyPath()!=null)
                message = new ResponseError(violation.getPropertyPath().toString(),violation.getMessage()).toString();
            errors.add(message);
        }
        return errors;
    }

}
