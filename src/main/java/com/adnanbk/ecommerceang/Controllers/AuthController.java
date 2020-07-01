package com.adnanbk.ecommerceang.Controllers;


import com.adnanbk.ecommerceang.Jwt.JwtTokenUtil;
import com.adnanbk.ecommerceang.dto.JwtResponse;
import com.adnanbk.ecommerceang.models.ApiError;
import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.models.ResponseError;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
import com.adnanbk.ecommerceang.services.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {


    private AuthService authService;


    public AuthController( AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> create(@RequestBody @Valid AppUser user)   {

       JwtResponse jwtResponse= authService.handleRegister(user);
        return new ResponseEntity(jwtResponse,HttpStatus.CREATED);

    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody  AppUser appUser) {


try {
    JwtResponse jwtResponse = authService.handleLogin(appUser);
    return new ResponseEntity(jwtResponse,HttpStatus.OK);

}
catch (BadCredentialsException e)
{
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());

}


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
}
