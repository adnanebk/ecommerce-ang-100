package com.adnanbk.ecommerceang.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUserValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface UniqueUser {


     String message() default "Already exists";

     Class<?>[] groups() default {};

     Class<? extends Payload>[] payload() default{};
}
