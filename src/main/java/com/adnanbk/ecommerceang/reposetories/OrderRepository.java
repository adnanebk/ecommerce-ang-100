package com.adnanbk.ecommerceang.reposetories;

import com.adnanbk.ecommerceang.entity.UserOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin
public interface OrderRepository extends CrudRepository<UserOrder, Integer> {


}
