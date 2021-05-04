package com.adnanbk.ecommerceang.services.imp;

import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.models.CreditCard;
import com.adnanbk.ecommerceang.models.UserOrder;
import com.adnanbk.ecommerceang.reposetories.CreditCardRepo;
import com.adnanbk.ecommerceang.reposetories.OrderItemRepo;
import com.adnanbk.ecommerceang.reposetories.OrderRepository;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
import com.adnanbk.ecommerceang.services.UserOderService;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserOrderServiceImp implements UserOderService {
    private OrderRepository orderRepository;
    private UserRepo userRepo;
    private OrderItemRepo orderItemRepo;
    private CreditCardRepo creditCardRepo;
    private Validator validator;

    public UserOrderServiceImp(OrderRepository orderRepository, UserRepo userRepo, OrderItemRepo orderItemRepo, CreditCardRepo creditCardRepo, Validator validator) {
        this.orderRepository = orderRepository;
        this.userRepo = userRepo;
        this.orderItemRepo = orderItemRepo;
        this.creditCardRepo = creditCardRepo;
        this.validator = validator;
    }

    @Override
    public List<UserOrder> findAllByUserName(String userName) {
        List<UserOrder> userOrders=orderRepository.findByAppUser_UserName(userName);
       return userOrders;
    }

    @Override
    public UserOrder saveOrder(UserOrder userOrder, String userName) {
        AppUser appUser =userRepo.findByUserName(userName);
        CreditCard creditCard=userOrder.getCreditCard();
        if(creditCard==null)
            throw new ValidationException("you must enter a credit card");
        var errors= validator.validate(creditCard);
        if(!errors.isEmpty())
                throw new ConstraintViolationException(errors);
        int year=2000+Integer.parseInt(creditCard.getExpirationDate().split("/")[1]);
        int month=Integer.parseInt(creditCard.getExpirationDate().split("/")[0]);
        LocalDate expirationDate= LocalDate.of(year,month,1);
        if(expirationDate.isBefore(LocalDate.now()))
            throw new ValidationException("your credit card has expired");
        var userCardOptional=creditCardRepo.findByCardNumber(creditCard.getCardNumber());
        creditCard.setAppUser(appUser);
         var userCard=  userCardOptional.orElse(creditCardRepo.save(creditCard));
        userOrder.setAppUser(appUser);
        userOrder.setCreditCard(userCard);
        userOrder.setUserOrderItems(orderItemRepo.saveAll(userOrder.getOrderItems()));
      return  orderRepository.save(userOrder);
    }

}
