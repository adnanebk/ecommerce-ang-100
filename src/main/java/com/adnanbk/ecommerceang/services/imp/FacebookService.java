package com.adnanbk.ecommerceang.services.imp;


import com.adnanbk.ecommerceang.dto.JwtResponse;
import com.adnanbk.ecommerceang.dto.LoginUserDto;
import com.adnanbk.ecommerceang.dto.RegisterUserDto;
import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
import com.adnanbk.ecommerceang.services.AuthService;
import com.adnanbk.ecommerceang.services.SocialService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacebookService implements SocialService {


    @Value("${facebook.clientId}")
    private String clientId;
    @Value("${facebook.clientSecret}")
    private String clientSecret;
    private final String apiInspector ="https://graph.facebook.com/debug_token";
    private UserRepo userRepo;
    @Value("${social.password}")
    private String password;
    private RestTemplate restTemplate;
    private AuthService authService;

    public FacebookService(UserRepo userRepo, RestTemplate restTemplate, AuthService authService) {
        this.userRepo = userRepo;
        this.restTemplate = restTemplate;
        this.authService = authService;
    }


    public JwtResponse verify(JwtResponse jwtResponse)  {
        String token=jwtResponse.getToken();
        RegisterUserDto user=jwtResponse.getAppUser();
        if(token==null)
            throw new BadCredentialsException("Invalid credentials");
        String accessToken=clientId+'|'+clientSecret;
        ResponseEntity<String> response=  restTemplate.getForEntity(apiInspector +"?input_token={token}&access_token={app-token}"
                        ,String.class,token,accessToken);

            if (response.getStatusCode().is2xxSuccessful()) {
                if(!userRepo.existsByUserName(user.getUserName())){
                    AppUser appUser=new AppUser(user.getUserName(),user.getEmail(),user.getFirstName(),user.getLastName(),password);
                    return authService.handleRegister(appUser);
                }
                else
                {
                    LoginUserDto loginUserDto=new LoginUserDto(user.getUserName(),password);
                    return authService.handleLogin(loginUserDto);
                }

            } else {
                System.out.println("Invalid ID token.");
                throw new BadCredentialsException("Invalid credentials");
            }

    }

}
