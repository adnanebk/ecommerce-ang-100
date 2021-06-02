package com.adnanbk.ecommerceang.services;

import com.adnanbk.ecommerceang.models.CreditCard;
import com.adnanbk.ecommerceang.models.UserOrder;

import java.util.List;

public interface UserOderService {

    UserOrder saveOrder(UserOrder userOrder, String userName);

}
