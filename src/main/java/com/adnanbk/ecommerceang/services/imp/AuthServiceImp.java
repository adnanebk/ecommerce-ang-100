package com.adnanbk.ecommerceang.services.imp;

import com.adnanbk.ecommerceang.Jwt.JwtTokenUtil;
import com.adnanbk.ecommerceang.dto.JwtResponse;
import com.adnanbk.ecommerceang.dto.LoginUserDto;
import com.adnanbk.ecommerceang.dto.RegisterUserDto;
import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
import com.adnanbk.ecommerceang.services.AuthService;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImp implements AuthService {

    private  UserRepo userRepo;
    private final JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncode;
    private AuthenticationManager authenticationManager;


    public AuthServiceImp(UserRepo userRepo, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncode, AuthenticationManager authenticationManager) throws BadCredentialsException {
        this.userRepo = userRepo;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncode = passwordEncode;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtResponse handleLogin(LoginUserDto appUser){
      var currentUser  = userRepo.findByUserName(appUser.getUserName());
       if (currentUser==null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(appUser.getUserName(), appUser.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (BadCredentialsException e) {

            throw new BadCredentialsException("Invalid username or password");

        }


        final String token = jwtTokenUtil.generateToken(appUser.getUserName(),generateClaims(currentUser));
        RegisterUserDto registerUserDto = new RegisterUserDto();
        BeanUtils.copyProperties(currentUser,registerUserDto);
        return new JwtResponse(token,registerUserDto);
    }
    @Override
    public JwtResponse handleRegister(AppUser user)
    {
        user.setPassword(passwordEncode.encode(user.getPassword()));
        user= userRepo.save(user);
        String token = this.jwtTokenUtil.generateToken(user.getUserName(),generateClaims(user));
        RegisterUserDto registerUserDto = new RegisterUserDto();
        BeanUtils.copyProperties(user,registerUserDto);
      return   new JwtResponse(token,registerUserDto);
    }

   private HashMap<String,Object> generateClaims(AppUser appUser){
       var claims =new HashMap<String,Object>();
       claims.put("email",appUser.getEmail());

       return claims;
   };
}
