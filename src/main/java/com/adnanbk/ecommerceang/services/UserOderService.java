package com.adnanbk.ecommerceang.services;

import com.adnanbk.ecommerceang.models.UserOrder;

import java.util.List;

public interface UserOderService {
    List<UserOrder> findAllByUserName(String userName);

    UserOrder saveOrder(UserOrder userOrder, String userName);
}
