/*
package com.adnanbk.ecommerceang;

import com.adnanbk.ecommerceang.entity.UserOrder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeCreateOrderValidator")
public class OrderValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserOrder.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        UserOrder user = (UserOrder) obj;
        if (checkInputString(user.getFirstName())) {
            errors.rejectValue("fistName", "fistName.empty");
        }

        if (checkInputString(user.getEmail())) {
            errors.rejectValue("email", "email.empty");
        }
    }

    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }
}
*/
