package com.adnanbk.ecommerceang.validations;

import com.adnanbk.ecommerceang.reposetories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String> {


   private  UserRepo userRepo;



   @Autowired
   public UniqueUserValidator(UserRepo userRepo) {
      this.userRepo = userRepo;
   }

   public UniqueUserValidator() {

   }
   @Override
   public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
      if (s == null ) {
         return false;
      }
      if(userRepo==null)
         return true;

      boolean isValid = !userRepo.existsByUserName(s);
      return isValid;
   }
}
