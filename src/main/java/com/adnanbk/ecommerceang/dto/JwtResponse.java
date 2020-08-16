package com.adnanbk.ecommerceang.dto;

import com.adnanbk.ecommerceang.models.AppUser;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private RegisterUserDto appUser;

    public JwtResponse(String token,RegisterUserDto appUser) {
        this.token = token;
        this.appUser=appUser;
    }

}
