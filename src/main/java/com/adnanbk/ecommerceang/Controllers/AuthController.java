package com.adnanbk.ecommerceang.Controllers;


import com.adnanbk.ecommerceang.dto.JwtResponse;
import com.adnanbk.ecommerceang.dto.LoginUserDto;
import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.services.AuthService;
import com.adnanbk.ecommerceang.services.SocialService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {


    private AuthService authService;
    private SocialService googleService;
    private SocialService facebookService;


    public AuthController(AuthService authService, SocialService googleService, SocialService facebookService) {
        this.googleService = googleService;
        this.facebookService = facebookService;
        this.authService = authService;
    }

    @PostMapping(value = "/register")
    @ApiOperation(value = "register new user",response =JwtResponse.class )
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse create( @RequestBody @Valid AppUser user)   {
        return  authService.handleRegister(user);

    }
    @PostMapping("/login")
    public JwtResponse authenticateUser(@RequestBody LoginUserDto appUser) {
    return authService.handleLogin(appUser);
}

@PostMapping("/google")
public JwtResponse googleLogin(@RequestBody JwtResponse jwtResponse){
            return googleService.verify(jwtResponse);

}
    @PostMapping("/facebook")
    public JwtResponse facebookLogin(@RequestBody JwtResponse jwtResponse) {
        return facebookService.verify(jwtResponse);
    }

}
