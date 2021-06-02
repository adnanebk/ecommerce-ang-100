package com.adnanbk.ecommerceang.services.imp;

import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.models.CreditCard;
import com.adnanbk.ecommerceang.reposetories.CreditCardRepo;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
import com.adnanbk.ecommerceang.services.CreditCardService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private CreditCardRepo creditCardRepo;
    private UserRepo  userRepo;

    public CreditCardServiceImpl(CreditCardRepo creditCardRepo, UserRepo userRepo) {
        this.creditCardRepo = creditCardRepo;
        this.userRepo = userRepo;
    }

    @Override
    public CreditCard saveCard(CreditCard creditCard, String userName) {
        AppUser user=userRepo.findByUserName(userName);
        creditCard.setAppUser(user);
       return creditCardRepo.save(creditCard);
    }


}
