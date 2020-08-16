package com.adnanbk.ecommerceang.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class RegisterUserDto {

    private String userName;

    private String email;

    private String firstName;

    private String lastName;

    private String adress;

    private String city;
    private String country;
}
