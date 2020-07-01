package com.adnanbk.ecommerceang.reposetories;

import com.adnanbk.ecommerceang.models.UserOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;


@CrossOrigin
public interface OrderRepository extends CrudRepository<UserOrder, Integer> {
    @RestResource(path="byUserName")


   List<UserOrder> findByAppUserUserName(String userName);

}
