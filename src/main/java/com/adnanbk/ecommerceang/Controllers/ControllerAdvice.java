package com.adnanbk.ecommerceang.Controllers;

import com.adnanbk.ecommerceang.dto.ApiError;
import com.adnanbk.ecommerceang.dto.ResponseError;
import com.adnanbk.ecommerceang.exceptions.InvalidTokenException;
import com.adnanbk.ecommerceang.exceptions.UserNotEnabledException;
import com.adnanbk.ecommerceang.models.Product;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {


    @ExceptionHandler({ PersistenceException.class,ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolation(RuntimeException ex) {
        System.out.println("******persistence exception******");
        if(ex  instanceof ConstraintViolationException  cause) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Try to fix these errors", generateErrors(cause));
            return ResponseEntity.badRequest().body(apiError);
        }
        if(NestedExceptionUtils.getRootCause(ex)  instanceof ConstraintViolationException cause) {
            for (var constr : cause.getConstraintViolations()) {
                String property="";
                if (constr.getPropertyPath() != null)
                    property=constr.getPropertyPath().toString();

                    return ResponseEntity.badRequest().body(new RuntimeException(property+" "+constr.getMessage()));

            }
        }
        if(NestedExceptionUtils.getRootCause(ex) instanceof SQLIntegrityConstraintViolationException cause)
        {
            return returnUniqueErrorMessage(cause);
        }
        if(ex  instanceof DataIntegrityViolationException cause) {

            return returnUniqueErrorMessage(cause);
        }
        return ResponseEntity.badRequest().body("An error has been thrown during database modification ");
    }

    private ResponseEntity<?> returnUniqueErrorMessage(Exception cause) {
        String message= cause.getMessage().toLowerCase();

        if(message.contains("product(name)"))
           message="product name already exists";
       else if(message.contains("product(sku)"))
           message="product sku already exists";
       else if(message.contains("product_category(name)"))
            message="category name already exists";
       else
           message="An error has been thrown during database modification";

        return ResponseEntity.badRequest().body(new RuntimeException(message));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Set<Object> errors = new HashSet<>();

        ex.getBindingResult().getFieldErrors().forEach(
                        er->
                        errors.add(new ResponseError(er.getField(),er.getDefaultMessage()))
        );

       ex.getBindingResult().getGlobalErrors()
                .forEach(x -> {
                    if(x.getDefaultMessage()!=null)
                        errors.add(new ResponseError(Objects.requireNonNull(x.getCode()),x.getDefaultMessage()));
                          else
                           errors.add(x.getCode());
                });
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Try to fix these errors", errors);
        return   ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        return   ResponseEntity.badRequest().body(ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> BadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.badRequest().body(ex);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseBody
    public String InvalidTokenException(InvalidTokenException ex) {
        return "<h2>"+ex.getMessage()+"</h2>";
    }

    @ExceptionHandler(UserNotEnabledException.class)
    public ResponseEntity<?> UserNotEnabledException(UserNotEnabledException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex);
    }
    private Set<ResponseError> generateErrors(ConstraintViolationException cause) {
        Set<ResponseError> errors = new HashSet<>();
        for (ConstraintViolation<?> violation : cause.getConstraintViolations()) {
            if(violation.getPropertyPath()!=null)
                errors.add(new ResponseError(violation.getPropertyPath().toString(),violation.getMessage()));
        }
        return errors;
    }
}
