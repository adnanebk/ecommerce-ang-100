package com.adnanbk.ecommerceang.Controllers;


import com.adnanbk.ecommerceang.dto.JwtResponse;
import com.adnanbk.ecommerceang.dto.ApiError;
import com.adnanbk.ecommerceang.dto.LoginUserDto;
import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.dto.ResponseError;
import com.adnanbk.ecommerceang.services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
    @ApiOperation(value = "register new user",response =JwtResponse.class )
    public ResponseEntity<?> create( @RequestBody @Valid AppUser user)   {

       JwtResponse jwtResponse= authService.handleRegister(user);
        return new ResponseEntity(jwtResponse,HttpStatus.CREATED);

    }
    @PostMapping("/login")
    @ApiOperation(value = "Login the user",response =JwtResponse.class )
    public ResponseEntity<?> authenticateUser(@RequestBody LoginUserDto appUser) {


try {
    JwtResponse jwtResponse = authService.handleLogin(appUser);
    return new ResponseEntity(jwtResponse,HttpStatus.OK);

}
catch (BadCredentialsException e)
{
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());

}


}




}
