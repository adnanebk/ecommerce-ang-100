package com.adnanbk.ecommerceang.services;

import com.adnanbk.ecommerceang.Jwt.JwtTokenUtil;
import com.adnanbk.ecommerceang.dto.JwtResponse;
import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private  UserRepo userRepo;
    private final JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncode;
    private AuthenticationManager authenticationManager;


    public AuthService(UserRepo userRepo, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncode, AuthenticationManager authenticationManager) throws BadCredentialsException {
        this.userRepo = userRepo;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncode = passwordEncode;
        this.authenticationManager = authenticationManager;
    }

    public JwtResponse handleLogin(AppUser appUser){
      var currentUser  = userRepo.findByUserName(appUser.getUserName());
       if (currentUser==null) {
            throw new BadCredentialsException("Invalid username");

        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(appUser.getUserName(), appUser.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            System.out.printf("");
        }
        catch (BadCredentialsException e) {

            throw new BadCredentialsException("Invalid password");

        }

        final String token = jwtTokenUtil.generateToken(appUser.getUserName());
        return new JwtResponse(token,currentUser);
    }
    public JwtResponse handleRegister(AppUser user)
    {
        user.setPassword(passwordEncode.encode(user.getPassword()));
        user= userRepo.save(user);
        String token = this.jwtTokenUtil.generateToken(user.getUserName());
      return   new JwtResponse(token,user);
    }
}
